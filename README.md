# StockAnalyzer

Die Klasse Stock.java analysiert die Aktienkurse, welche im JSON Format gegeben sind.
Als Grundlage für das Arbeiten mit JSON-Objekten, wird das Open-Source Projekt
https://github.com/stleary/JSON-java
genutzt.

In der Klasse Stock.java wird der Tag mit dem niedrigsten Kurs, dem höchsten Kurs und dem größten Unterschied zwischen Eröffnungskurs und Schlusskurs ermittelt und zudem der durchschnittliche Schlusskurs der Aktie berechnet.
Man achte darauf, dass nur ein Datum ausgegeben wird, auch wenn die Lösung nicht eindeutig ist. 

Die Testklasse StockTest testet, ob die Funktionen auch bei zufälligen 
Werten das richtige Ergebnis ermittelt wird.
