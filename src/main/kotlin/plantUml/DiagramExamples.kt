package com.pawlowski.plantUml

object DiagramExamples {
    val diagram1 =
        """
        @startuml
        left to right direction

        actor "Pracownik Sklepu" as Employee

        rectangle "System Zarządzania Magazynem Sklepu" {
          usecase "Dodaj Produkt" as AddProduct
          usecase "Edytuj Produkt" as EditProduct
          usecase "Usuń Produkt" as DeleteProduct
          usecase "Zmień Stan/Ilość Produktu" as ChangeProductStatus
          usecase "Wyświetl Informacje o Produkcie" as ViewProductInfo
        }

        Employee --> AddProduct
        Employee --> EditProduct
        Employee --> DeleteProduct
        Employee --> ChangeProductStatus
        Employee --> ViewProductInfo

        @enduml
        """.trimIndent()

    val incorrectDiagram1 =
        """
        wrong diagram content
        """.trimIndent()

    val incorrectDiagram2 =
        """
        @startuml
        left to right direction

        actor "Pracownik Sklepu" as Employee

        rectngle "System Zarządzania Magazynem Sklepu" {
          usecase "Dodaj Produkt" as AddProduct
          usecase "Edytuj Produkt" as EditProduct
          usecase "Usuń Produkt" as DeleteProduct
          usecase "Zmień Stan/Ilość Produktu" as ChangeProductStatus
          usecase "Wyświetl Informacje o Produkcie" as ViewProductInfo
        }

        Employee --> AddProduct
        Employee --> EditProduct
        Employee --> DeleteProduct
        Employee --> ChangeProductStatus
        Employee --> ViewProductInfo

        @enduml
        """.trimIndent()
}
