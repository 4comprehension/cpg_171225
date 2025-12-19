# Zadanie domowe

Celem zadania jest stworzenie nowego serwisu RESTowego oraz jego integracja i przetestowanie w istniejącym systemie.

Do tej pory temat pobierania opisów filmów był celowo pominięty. W tym zadaniu utworzycie serwis odpowiedzialny za operacje na filmach - dodawanie, aktualizowanie i pobieranie opisów. Serwis będzie wykorzystywany przez klienta, który będzie testowany w ramach zadania.

## Wymagania dla nowego serwisu `movie-descriptions`

1. Stwórz serwis **od zera** z prostym REST API, pozwalającym na:
    - dodawanie nowych filmów,
    - aktualizowanie opisów filmów,
    - pobieranie opisów filmów.

2. Dla uproszczenia przyjmujemy, że do pobierania opisu można używać `movieId`.

3. Serwis powinien przechowywać opisy filmów w bazie danych (H2 do testów).

4. Zamiast testów black-box, utwórz klasyczne testy Springowe wykorzystujące bazę H2.

## Integracja z istniejącym serwisem `rental-store`

1. Zaimplementuj nowe repozytorium realizujące interfejs `MovieDescriptionsRepository`.

2. Nowa implementacja powinna działać **na profilu produkcyjnym**, natomiast obecna 'zaślepka' pozostaje aktywna **na profilu developerskim**.

## Rozbudowa istniejącego testu black-box

1. Dodaj odpowiednie stuby dla zewnętrznego serwisu przy użyciu **WireMock**.
2. Rozszerz asercje tak, aby test weryfikował poprawność integracji z nowym serwisem.

Najważniejsza jest sama integracja - warto się na niej skupić w pierwszej kolejności. Na początek nowy serwis może zwracać puste opisy filmów; takie rozwiązanie wystarczy do przetestowania integracji. 
Dopiero później można dodać persystencję, testy i inne funkcjonalności.
