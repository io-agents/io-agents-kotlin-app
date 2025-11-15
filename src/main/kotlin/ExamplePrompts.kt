package com.pawlowski

import com.pawlowski.useCase.UseCaseDiagramInput

object ExamplePrompts {
    val prompt1 =
        UseCaseDiagramInput(
            plainTextUseCaseDescription =
                """
                Use Case: Rejestracja nowego użytkownika

                Aktor: Użytkownik (niezalogowany)
                Cel: Założenie konta w księgarni, aby móc składać zamówienia.
                
                Scenariusz główny:
                
                Użytkownik wybiera opcję „Zarejestruj się”.
                
                System wyświetla formularz rejestracyjny.
                
                Użytkownik wprowadza dane (imię, e-mail, hasło).
                
                System weryfikuje poprawność danych i unikalność adresu e-mail.
                
                System tworzy konto i wysyła e-mail aktywacyjny.
                
                Użytkownik aktywuje konto przez kliknięcie w link.
                
                System potwierdza aktywację konta.
                
                Alternatywy:
                
                4a. E-mail już istnieje — system wyświetla komunikat o błędzie.
                
                5a. Błąd serwera — system informuje użytkownika i prosi o ponowną próbę później.
                """.trimIndent(),
        )

    val prompt2 =
        UseCaseDiagramInput(
            plainTextUseCaseDescription =
                """
                Use Case: Rejestracja nowego użytkownika

                Aktor: Użytkownik (niezalogowany)
                Cel: Założenie konta w księgarni, aby móc składać zamówienia.
                
                Scenariusz główny:
                
                Użytkownik wybiera opcję „Zarejestruj się”.
                
                System wyświetla formularz rejestracyjny.
                
                Użytkownik wprowadza dane (imię, e-mail, hasło).
                
                System weryfikuje poprawność danych i unikalność adresu e-mail.
                
                System tworzy konto i wysyła e-mail aktywacyjny.
                
                Użytkownik aktywuje konto przez kliknięcie w link.
                
                System potwierdza aktywację konta.
                
                Alternatywy:
                
                4a. E-mail już istnieje — system wyświetla komunikat o błędzie.
                
                5a. Błąd serwera — system informuje użytkownika i prosi o ponowną próbę później.
                """.trimIndent(),
        )

    val promptNotEnough =
        UseCaseDiagramInput(plainTextUseCaseDescription = "Zrób aplikację do zarządzania")
}
