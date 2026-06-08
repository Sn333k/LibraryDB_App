# ==========================================
# KONFIGURACJA TESTU
# ==========================================
# ==========================================
# KONFIGURACJA TESTU
# ==========================================
# Przykładowy URL z parametrami - możesz je zostawić puste: /search?title=&author=&library=
$URL = "http://localhost:8080/books-authors/search?author=Liza%20Hadley&title=Not%20Such%20a%20Stranger&library=Warszawa"

$StartConcurrency = 1
$MaxConcurrency = 5000
$Step = 1
$Multiplier = 10

$OutputFile = "Wyniki_Testu_Search.csv"
# ==========================================



"Wspolbieznosc;Calkowita_Liczba_Zapytan;Zapytan_na_sekunde(RPS);Czas_na_zadanie(ms);Bledy" | Out-File $OutputFile -Encoding UTF8

Write-Host "Rozpoczynam dynamiczny test obciazeniowy dla: $URL" -ForegroundColor Cyan

for ($c = $StartConcurrency; $c -le $MaxConcurrency; $c += $Step) {

    # Obliczamy dynamiczną liczbę zapytań na podstawie aktualnej liczby użytkowników
    $n = $c * $Multiplier

    Write-Host "Krok: $c uzytkownikow wysyla $n zapytan..." -NoNewline

    # Uruchomienie AB w tle z dynamicznym $n i $c
    $command = "ab -n $n -c $c `"$URL`""
    $output = Invoke-Expression $command

    # Wyciaganie danych
    $rpsMatch = [regex]::Match($output, 'Requests per second:\s+([\d\.]+)')
    $failMatch = [regex]::Match($output, 'Failed requests:\s+(\d+)')
    $timeMatch = [regex]::Match($output, 'Time per request:\s+([\d\.]+)')

    # Zmiana kropek na przecinki
    $rps = if ($rpsMatch.Success) { $rpsMatch.Groups[1].Value.Replace('.', ',') } else { "0" }
    $time = if ($timeMatch.Success) { $timeMatch.Groups[1].Value.Replace('.', ',') } else { "0" }
    $fails = if ($failMatch.Success) { $failMatch.Groups[1].Value } else { "0" }

    # Wyswietlanie w konsoli
    if ([int]$fails -gt 0) {
        Write-Host " [RPS: $rps | Czas: $time ms | Bledy: $fails]" -ForegroundColor Red
    } else {
        Write-Host " [RPS: $rps | Czas: $time ms | Bledy: $fails]" -ForegroundColor Green
    }

    # Zapis do pliku CSV (dodano kolumne pokazujaca ile bylo zapytan)
    "$c;$n;$rps;$time;$fails" | Out-File $OutputFile -Append -Encoding UTF8

    Start-Sleep -Seconds 2
}

Write-Host "`nTest zakonczony! Wyniki zapisano w: $OutputFile" -ForegroundColor Cyan