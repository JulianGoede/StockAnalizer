# StockAnalizer

Die Klasse Stock.java analysiert die Aktienkurse, welche im JSON Format gegeben sind.
Als Grundlage für das Arbeit mit JSON-Objekten wird das Open-Source Projekt
https://github.com/stleary/JSON-java
genutzt.

In der Klasse Stock.java werden die folgenden Werte ermittelt:
  -den Tag mit dem niedrigsten Kurs
  -den Tag mit dem höchsten Kurs
  -den Tag mit dem größten Unterschied zwischen Eröffnungskurs und Schlusskurs
  -den durchschnittlichen Schlusskurs der Aktie

Die Testklasse StockTest testet, ob die Funktionen auch bei zufälligen 
Werten das richtige Ergebnis ermittelt wird.
