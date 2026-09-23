# Rahmenplan – Patterns and Frameworks

Stand: 21.09.2026 · Wintersemester 2026/27

## Unser Projekt

Wir entwickeln ein Multiplayer-Spiel. Unser aktueller Entwurf ist **Mastermind: Cipher Chase**. Das Thema müssen wir noch mit der Lehrperson abstimmen.

Wir bauen einen grafischen Client, der bei beiden Spielern läuft, einen Java-Server und eine relationale Datenbank. Beide Spielrollen stecken in ihren eigenen Clients. 

Unser Ziel ist eine vollständige, gut erklärbare Anwendung mit überschaubarem Spielumfang. Neben dem Spiel brauchen wir Zeit für Konten, Lobby, Datenbank, Schnittstellen und die Abgaben.

## Spielidee: Cipher Chase

Ein Dieb flieht durch ein Gebäude, ein Polizist verfolgt ihn. Um Türen zu öffnen, knacken beide Zahlencodes nach dem Mastermind-Prinzip. Der Dieb hinterlässt seine bisherigen Eingaben als Spuren. Der Polizist entscheidet, ob er Zeit in diese Hinweise investiert oder selbst rätselt.

### Grundregeln aus der bisherigen Idee

- Es gibt insgesamt sechs Türen.
- Jeder Code besteht aus vier Zahlen zwischen 1 und 6. Zahlen dürfen mehrfach vorkommen.
- Grün bedeutet: richtige Zahl an der richtigen Position. Orange bedeutet: richtige Zahl an der falschen Position.
- Erst bei vier grünen Treffern öffnet sich die Tür.
- Die Figur geht durch die Tür und weiter zur nächsten. Die Tür hinter ihr schließt sich automatisch. Dafür sind kurze Videosequenzen vorgesehen.
- Der Dieb hat einen Vorsprung. Bisherige Beschreibung: ein Raum beziehungsweise zwei Türen. Die genaue Aufstellung legen wir noch fest.
- Der Dieb gewinnt, wenn er die letzte Tür vor Ablauf des Countdowns passiert.
- Läuft der Countdown vorher ab, stürmt das SEK das Gebäude und die Polizei gewinnt.

### Spuren und Eingabehistorie

Der Dieb hinterlässt seine Eingaben mit den jeweiligen Auswertungen. Der Polizist kann die Versuche einzeln ansehen und erst nach zwei Sekunden zum nächsten weiterschalten. Das Anschauen kostet Zeit, die ihm zum eigenen Rätseln fehlt.

Für die erste Umsetzung schlagen wir vor:

- Beide lösen an derselben Tür denselben Code, damit die Spuren nutzbar sind.
- Nur fehlgeschlagene Versuche bleiben als Spuren sichtbar. Der erfolgreiche Versuch würde die komplette Lösung verraten.
- Beim Betrachten läuft die Spielzeit normal weiter. Es gibt zunächst keinen zusätzlichen Zeitabzug.
- Der Polizist kann die Ansicht jederzeit verlassen und selbst einen Code eingeben.
- Die Rückmeldung steht als Gruppe von Markern neben dem Versuch. Sie zeigt nicht direkt, welche eingegebene Stelle richtig ist.
- Wiederholte Zahlen werden nicht doppelt gewertet: zuerst exakte Treffer, danach richtige Zahlen an anderer Position. Symbole ergänzen die Farben.

Damit lohnt sich die History nur, wenn die gewonnenen Informationen den Zeitaufwand ausgleichen. Ob zwei Sekunden pro Schritt passen, müssen wir ausprobieren.

### Gadgets

Die ursprüngliche Idee enthält vier Gadgets:

- **Dietrich für den Dieb:** Deckt eine Stelle des Codes auf.
- **Rauchbombe für den Dieb:** Entfernt alle Spuren im letzten Raum. Die Polizei kann dort keine Eingabehistorie mehr abrufen.
- **Alarm für den Polizisten:** Ersetzt den Code, an dem der Dieb gerade arbeitet, vollständig durch einen neuen Code.
- **Fingerabdruck für den Polizisten:** Deckt eine Zahl des Codes auf.

Vorschläge für den ersten Test, noch nicht beschlossen:

- Jedes Gadget lässt sich einmal pro Partie einsetzen.
- Der Dietrich verrät Zahl und Position, der Fingerabdruck nur eine enthaltene Zahl ohne Position.
- Der Alarm verändert nur eine zufällige Stelle. Ein kompletter Austausch könnte zu viel Fortschritt auf einmal vernichten.
- Nach einem Alarm wird klar angezeigt, dass bisherige Versuche zum alten Code gehören. Was mit bereits aufgedeckten Hinweisen und späteren Spuren passiert, müssen wir ebenfalls festlegen.

### Was wir an den Regeln noch klären müssen

- **Einholen:** Vorschlag: Die Polizei gewinnt auch, sobald sie den Raum des Diebs erreicht. So hat das eigene Vorankommen ein direktes Ziel. Der genaue Fangzeitpunkt muss auch bei Türübergängen eindeutig sein.
- **Startpositionen:** Ein möglicher Start wäre Polizei vor Tür 1 und Dieb vor Tür 3. Dafür fehlen zunächst echte Spuren an den ersten beiden Türen. Eine Startphase, in der der Dieb diese Türen bereits löst, wäre eine Möglichkeit. Dabei müssen wir auch den Beginn des Countdowns festlegen.
- **Zeit und Eingaben:** Wie lange dauert eine Partie? Gibt es eine Pause zwischen eigenen Versuchen oder ein Versuchslimit?
- **History:** Wann wird ein Versuch sichtbar, in welcher Reihenfolge werden Spuren gezeigt und kann der Polizist bereits gelesene Versuche erneut ansehen?
- **Gadgets:** Vollständiger oder teilweiser Codewechsel beim Alarm? Welcher Raum ist Ziel der Rauchbombe, und wie gehen wir mit bereits gelesenen Spuren um?
- **Gleichzeitige Ereignisse:** Was gilt, wenn Codeeingabe, Alarm, Einholen oder Zeitablauf fast gleichzeitig eintreten? Der Server entscheidet die Reihenfolge.

### Umfang und erste Tests

Zuerst bauen wir Codeeingabe und Auswertung, sechs Türen, Vorsprung, Countdown und Spuren. Die Fangregel nehmen wir dazu, sobald sie feststeht. Für die Übergänge reicht am Anfang eine kurze Türanimation; Videos kommen später.

Danach ergänzen wir die Gadgets. Wir testen mit vertauschten Rollen, ob der Polizist den Vorsprung aufholen kann und ob sich das Anschauen der Spuren lohnt. Countdown, Wartezeiten und Gadget-Stärke passen wir anhand dieser Partien an.

Die Idee passt gut zu unserem Projekt: Die Rätselregeln bleiben überschaubar, während die Verfolgung und die Spuren für Interaktion sorgen. Das größte offene Thema ist die Spielbalance.

## Was die fertige Anwendung können muss

Die folgenden Funktionen ergeben sich aus den Projektunterlagen. Bei den zusätzlichen Vorgaben aus dem Lübecker Leitfaden klären wir noch, ob sie auch für Emden gelten.

- Zwei angemeldete Personen können eine vollständige Partie spielen.
- Registrierung mit Benutzername und Passwort, Login und Logout funktionieren.
- Spieler finden sich über eine Lobby und starten gemeinsam eine Partie.
- Abgeschlossene Partien werden gespeichert. Jeder sieht seine Spielhistorie und einfache Auswertungen, etwa Siege und Niederlagen. Diese Spielhistorie ist getrennt von den Eingabespuren innerhalb einer Partie.
- Bilder werden sinnvoll über die API übertragen und im Client angezeigt. Hierfür eignen sich zB Profilbild-Upload und -Abruf. Fest eingebaute Spielgrafiken allein reichen dafür nicht.
- Alle Funktionen sind über die grafische Oberfläche bedienbar.

Singleplayer, Chat, globale Ranglisten, KI-Gegner, mehrere Spielmodi und aufwendige Grafik gehören vorerst nicht zum Umfang.

## Technischer Plan

### Grundlagen

- Client und Server sind getrennte Komponenten.
- Der Server wird in Java umgesetzt.
- Benutzer und Spielergebnisse liegen in einer relationalen Datenbank und werden über ein ORM verwaltet. Das Datenbankprodukt ist noch offen.
- Die API unterstützt JWT; die allgemeine Prüfungsbeschreibung verlangt diese Unterstützung.
- Wir setzen Entwurfs- und Architekturmuster dort ein, wo sie uns helfen, und können ihren Nutzen erklären. Eine feste Anzahl ist in den Unterlagen nicht genannt.
- Synchrone und asynchrone Kommunikation sowie sinnvolle Parallelverarbeitung gehören zum Projekt. Netzwerkzugriffe dürfen die Oberfläche nicht blockieren.

### Geplante Werkzeuge

Wir orientieren uns zunächst am Leitfaden: Spring Boot, Spring Security mit JWT, JPA, Git und Maven. REST mit JSON verwenden wir für Konten, Profilbilder und Spielhistorie; WebSocket mit STOMP für Lobby und Spielaktionen. Die Versionen legen wir beim Projektstart fest.

Die Client-Technologie ist noch offen. Die allgemeine Prüfungsbeschreibung nennt JavaFX als Standard und Android Java API als Alternative. Einen Web-Client wählen wir erst, wenn das geklärt ist. Bei Vue oder Angular verlangt der Leitfaden eine SPA.

### Zuständigkeiten im Spiel

Der Server verwaltet Codes, Countdown, Positionen, Versuche und Gadgets. Er prüft Aktionen und entscheidet über Türöffnung und Spielende. Jeder Client bekommt nur die Informationen, die seine Rolle sehen darf; geheime Codes werden nicht vollständig an die Clients geschickt.

Mögliche Ansatzpunkte für Patterns sind Spielphasen, Spielaktionen und Gadget-Verhalten. Die konkrete Auswahl treffen wir bei der Modellierung.

### Mit der Lehrperson klären

- Gilt der ausdrücklich auf Lübeck bezogene Leitfaden auch für uns in Emden? Das betrifft besonders die freie Client-Wahl und die zusätzlichen Vorgaben zu Spring Boot, Spring Security, STOMP, Git/Maven und Game-Engines.
- Ist Mastermind: Cipher Chase als Thema in Ordnung?

## Termine und Abgaben

Die Termine kommen aus dem Kursplan. Die Arbeitsschritte darunter sind unsere interne Planung.

- **28.09.2026 – Gruppenwahl:** Teammitglieder festhalten.
- **05.10.2026 – M0, Spielauswahl:** Cipher Chase mit klaren Regeln und begrenztem Umfang beschreiben, offene Fragen klären und das Thema abstimmen.
- **19.10.2026 – M1, UML-Klassendiagramm:** Fachliches Datenmodell mit Attributen, Beziehungen und Multiplizitäten entwerfen. Festlegen, welche Daten dauerhaft gespeichert werden und welche nur während einer Partie gebraucht werden.
- **02.11.2026 – M2, UML-Komponentendiagramm:** Client, Server, Datenbank und Schnittstellen darstellen. Framework-Auswahl, mögliche Patterns und Nebenläufigkeit begründen.
- **16.11.2026 – M3, Schnittstelle:** REST-Endpunkte und STOMP-Nachrichten mit Beispielen dokumentieren. Authentifizierung, Rollen, Bildübertragung, Fehlerfälle, Verbindungsabbruch und Spielende berücksichtigen.
- **04.01.2027 – M4, Prototyp:** Ausgewählte Funktionen in vorführbaren Vorversionen zeigen. Unser eigenes Ziel: Zwei Spieler können sich anmelden, zusammenfinden und mindestens eine Spielaktion über den Server austauschen. Vollständige Integration ist laut Unterlagen zu diesem Termin noch nicht nötig.
- **Finale Abgabe und Präsentation – Termin offen:** Vollständiges Projekt abgeben und gemeinsam erklären. Der 25.01.2027 ist im Kursplan nicht ausdrücklich als Abgabetermin festgelegt.

Für die finale Abgabe prüfen wir:

- [ ] Alle geforderten Funktionen laufen, einschließlich einer vollständigen Partie mit zwei Client-Instanzen.
- [ ] Ergebnisse, Auswertungen und Profilbilder funktionieren über Server und Datenbank.
- [ ] Ungültige Aktionen und Verbindungsabbrüche werden sinnvoll behandelt.
- [ ] Build, Konfiguration und Start sind dokumentiert; die Abgabe ist in der Versionsverwaltung eindeutig erkennbar.
- [ ] Diagramme und Schnittstellenbeschreibung passen zum fertigen Stand.
- [ ] Beide können die Architektur, ihre eigenen Beiträge und die eingesetzten Patterns erklären.
- [ ] Präsentation und Live-Demo sind geprobt.

Bewertet werden unter anderem Lauffähigkeit, Anforderungserfüllung, Codequalität, Architektur, Frameworks, Patterns, Kommunikation und Präsentation. Die Vorleistungen sind laut Leitfaden unbenotet. Die etwa 30 Minuten Präsentationszeit in der allgemeinen Prüfungsbeschreibung sind eine Empfehlung; unsere konkrete Dauer ist noch offen.

## Zusammenarbeit und nächste Schritte

Die Namen, die Aufgabenverteilung, das Repository und einen regelmäßigen Abstimmungstermin tragen wir noch ein. Alle sollen den Gesamtaufbau verstehen.

Als Nächstes:

- [ ] Offene Spielregeln durchgehen und eine Beispielpartie auf Papier spielen.
- [ ] Client-Technologie und übrige Werkzeuge festlegen.
- [ ] Repository und startbare Projektstruktur einrichten.
- [ ] Datenmodell und Schnittstellen gemeinsam entwerfen.
- [ ] Früh einen vollständigen Weg von der Codeeingabe im Client bis zur Serverantwort bauen.

Bisheriger Stand:

- **22.09.2026:** Festlegung des Teams, erste Kurzabsprache bzgl. des Gameplays und der Arbeitsverteilung
