# Rahmenplan – Patterns and Frameworks

Stand: 23.09.2026 · Wintersemester 2026/27

## Unser Projekt

Wir entwickeln zu dritt eine Abwandlung des Brettspielklassikers 'Mastermind' als Online-Multiplayer-Spiel.

Wir bauen einen Java-Server, eine relationale Datenbank und zwei grafische Clients: einen Desktop-Client mit JavaFX und einen Web-Client als Vue.js-SPA. Beide Clients nutzen dieselben Hochschulserver-Schnittstellen und bieten jeweils den vollen Funktionsumfang mit beiden Spielrollen. Deshalb können auch zwei Personen mit unterschiedlichen Clients gegeneinander spielen.

Unser Ziel ist eine vollständige, gut erklärbare Anwendung mit überschaubarem Spielumfang. Neben dem Spiel brauchen wir Zeit für Konten, Lobby, Datenbank, Schnittstellen und die Abgaben. Da jede Funktion in zwei Clients umgesetzt wird, halten wir den Spielumfang bewusst klein.

## Spielidee: 'Megamind: Cipher Chase'

Ein Dieb flieht durch ein Gebäude, ein Polizist verfolgt ihn. Um Türen zu öffnen, knacken beide Zahlencodes nach dem Mastermind-Prinzip. Der Dieb hinterlässt seine bisherigen Eingaben als Spuren. Der Polizist entscheidet, ob er Zeit in diese Hinweise investiert oder selbst rätselt.

### Grundregeln

- Es gibt insgesamt sechs Türen.
- Jeder Code besteht aus vier Zahlen zwischen 1 und 6. Zahlen dürfen mehrfach vorkommen.
- Grün bedeutet: richtige Zahl an der richtigen Position. Orange bedeutet: richtige Zahl an der falschen Position.
- Erst bei vier grünen Treffern öffnet sich die Tür.
- Die Figur geht durch die Tür und weiter zur nächsten. Die Tür hinter ihr schließt sich automatisch. Für diese gesamte Szene sind kurze Videosequenzen vorgesehen, keine eigenen Bewegungsabläufe.
- Der Dieb hat einen Vorsprung über einen Raum beziehungsweise zwei Türen. Die genaue Aufstellung legen wir noch fest.
- Der Dieb gewinnt, wenn er die letzte Tür vor Ablauf des Countdowns passiert.
- Läuft der Countdown vorher ab, stürmt das SEK das Gebäude und die Polizei gewinnt.
- Die Polizei gewinnt auch, sobald sie nach einer Türöffnung im selben Raum steht wie der Dieb. Öffnen beide fast gleichzeitig eine Tür, gilt die Reihenfolge, in der der Server die Eingaben verarbeitet.

### Spuren und Eingabehistorie

Der Dieb hinterlässt seine Eingaben mit den jeweiligen Auswertungen. Der Polizist kann die Versuche einzeln ansehen und erst nach zwei Sekunden zum nächsten weiterschalten. Das Anschauen kostet Zeit, die ihm zum eigenen Rätseln fehlt.

Für die erste Umsetzung gilt:

- Beide lösen an derselben Tür denselben Code, damit die Spuren nutzbar sind.
- Nur fehlgeschlagene Versuche bleiben als Spuren sichtbar. Der erfolgreiche Versuch würde die komplette Lösung verraten.
- Beim Betrachten läuft die Spielzeit normal weiter. Es gibt zunächst keinen zusätzlichen Zeitabzug.
- Der Polizist kann die Ansicht jederzeit verlassen und selbst einen Code eingeben.
- Die Rückmeldung steht als Gruppe von Markern neben dem Versuch. Sie zeigt nicht direkt, welche eingegebene Stelle richtig ist.
- Wiederholte Zahlen werden nicht doppelt gewertet: zuerst exakte Treffer, danach richtige Zahlen an anderer Position.

Damit lohnt sich die History nur, wenn die gewonnenen Informationen den Zeitaufwand ausgleichen. Ob zwei Sekunden pro Schritt passen, müssen wir ausprobieren.

### Gadgets

Jede Rolle hat zwei Gadgets. Jedes Gadget lässt sich einmal pro Partie einsetzen.

- **Dietrich für den Dieb:** Deckt eine Zahl des Codes mit ihrer Position auf.
- **Rauchbombe für den Dieb:** Entfernt alle Spuren im Raum, den der Dieb zuletzt vollständig gelöst hat. Die Polizei kann dort keine Eingabehistorie mehr abrufen.
- **Alarm für den Polizisten:** Ändert eine zufällige Stelle des Codes, an dem der Dieb gerade arbeitet.
- **Fingerabdruck für den Polizisten:** Deckt eine im Code enthaltene Zahl ohne Position auf.

Nach einem Alarm wird klar angezeigt, dass bisherige Versuche zum alten Code gehören. Bereits aufgedeckte Hinweise von Dietrich oder Fingerabdruck bleiben sichtbar, werden aber ebenfalls als veraltet markiert. Versuche nach dem Alarm beziehen sich auf den neuen Code und werden normal als Spuren hinterlassen.

### Was wir an den Regeln noch klären müssen

- **Startpositionen:** Ein möglicher Start wäre Polizei vor Tür 1 und Dieb vor Tür 3. Dafür fehlen zunächst echte Spuren an den ersten beiden Türen.
- **Zeit und Eingaben:** Wie lange dauert eine Partie? Gibt es eine Pause zwischen eigenen Versuchen oder ein Versuchslimit?
- **History:** Wann wird ein Versuch sichtbar, in welcher Reihenfolge werden Spuren gezeigt und kann der Polizist bereits gelesene Versuche erneut ansehen?
- **Gleichzeitige Ereignisse:** Was gilt, wenn Codeeingabe, Alarm oder Zeitablauf fast gleichzeitig eintreten? Entscheidet wie beim Einholen die Verarbeitungsreihenfolge auf dem Server?Einzelfälle müssen noch festgehalten werden.

### Umfang und erste Tests

Zuerst bauen wir Codeeingabe und Auswertung, sechs Türen, Vorsprung, Countdown, Spuren und die Fangregel. Für die Übergänge reicht am Anfang eine kurze Türanimation; Videos kommen später.

Danach ergänzen wir die Gadgets. Wir testen mit vertauschten Rollen und mit beiden Clients, ob der Polizist den Vorsprung aufholen kann und ob sich das Anschauen der Spuren lohnt. Countdown, Wartezeiten und Gadget-Stärke passen wir anhand dieser Partien an.

Die Idee passt gut zu unserem Projekt: Die Rätselregeln bleiben überschaubar, während die Verfolgung und die Spuren für Interaktion sorgen. Das größte offene Thema ist die Spielbalance.

## Was die fertige Anwendung können muss

- Zwei angemeldete Personen können eine vollständige Partie spielen, auch wenn eine den JavaFX-Client und die andere den Web-Client nutzt.
- Registrierung mit Benutzername und Passwort, Login und Logout funktionieren.
- Spieler finden sich über eine Lobby und starten gemeinsam eine Partie.
- Abgeschlossene Partien werden gespeichert. Jeder sieht seine Spielhistorie und einfache Auswertungen, etwa Siege und Niederlagen, Zeit und Anzahl der Versuche. Diese Spielhistorie ist getrennt von den Eingabespuren innerhalb einer Partie.
- Profilbilder (hochladen, abrufen) decken den Punkt der sinnvollen Übertragung über die API und Anzeige im Client ab.
- Alle Funktionen sind in beiden Clients über die grafische Oberfläche bedienbar.

## Technischer Plan

### Grundlagen

- Server und die beiden Clients sind getrennte Komponenten.
- Der Server wird in Java umgesetzt.
- Benutzer und Spielergebnisse liegen in einer relationalen Datenbank und werden über ein ORM verwaltet. Das Datenbankprodukt ist noch offen.
- Die API unterstützt JWT; die allgemeine Prüfungsbeschreibung verlangt diese Unterstützung.
- Die Server-Schnittstellen sind clientneutral: Beide Clients nutzen dieselben Endpunkte und Nachrichten, ohne Sonderwege für einen der beiden.
- Wir setzen Entwurfs- und Architekturmuster dort ein, wo sie uns helfen, und können ihren Nutzen erklären.
- Synchrone und asynchrone Kommunikation sowie sinnvolle Parallelverarbeitung gehören zum Projekt. Netzwerkzugriffe dürfen die Oberfläche nicht blockieren.

### Geplante Werkzeuge

Wir orientieren uns zunächst am Leitfaden: Spring Boot, Spring Security mit JWT, JPA, Git und Maven. REST mit JSON verwenden wir für Konten, Profilbilder und Spielhistorie; WebSocket mit STOMP für Lobby und Spielaktionen. Die Versionen legen wir beim Projektstart fest.

Clients:

- **Desktop-Client:** Java mit JavaFX.
- **Web-Client:** JavaScript mit Vue.js, umgesetzt als Single Page Application.

Beide Clients müssen mit unterschiedlichen Sprachen oder Frameworks und vollständig unabhängig voneinander entwickelt werden. Laut Leitfaden darf ein Client nicht durch Migration aus dem anderen entstehen. Gemeinsame Grundlage ist nur die dokumentierte Server-Schnittstelle. Für den Web-Client müssen wir zusätzlich den Zugriff aus dem Browser (CORS), die Ablage des JWT und den STOMP-Zugang über JavaScript einplanen.

### Zuständigkeiten im Spiel

Der Server verwaltet Codes, Countdown, Positionen, Versuche und Gadgets. Er prüft Aktionen und entscheidet über Türöffnung, Einholen und Spielende. Jeder Client bekommt nur die Informationen, die seine Rolle sehen darf; geheime Codes werden nicht vollständig an die Clients geschickt. Die Clients stellen den Zustand nur dar und schicken Aktionen; Spiellogik doppeln wir dort nicht. Das hält auch die beiden Client-Implementierungen schlank.

Mögliche Ansatzpunkte für Patterns sind Spielphasen, Spielaktionen und Gadget-Verhalten auf dem Server sowie die Trennung von Darstellung und Zustand in den Clients. Die konkrete Auswahl treffen wir bei der Modellierung.

## Termine und Abgaben

Die Termine kommen aus dem Kursplan. Die Arbeitsschritte darunter sind unsere interne Planung.

- **28.09.2026 – Gruppenwahl**
- **05.10.2026 – M0, Spielauswahl:** Cipher Chase mit klaren Regeln und begrenztem Umfang beschreiben, offene Fragen klären und das Thema abstimmen.
- **19.10.2026 – M1, UML-Klassendiagramm:** Fachliches Datenmodell mit Attributen, Beziehungen und Multiplizitäten entwerfen. Festlegen, welche Daten dauerhaft gespeichert werden und welche nur während einer Partie gebraucht werden.
- **02.11.2026 – M2, UML-Komponentendiagramm:** Server, Datenbank, beide Clients und die Schnittstellen darstellen. Framework-Auswahl für Server und beide Clients, mögliche Patterns und Nebenläufigkeit begründen.
- **16.11.2026 – M3, Schnittstelle:** REST-Endpunkte und STOMP-Nachrichten mit Beispielen dokumentieren. Authentifizierung, Rollen, Bildübertragung, Fehlerfälle, Verbindungsabbruch und Spielende berücksichtigen. Die Beschreibung muss für beide Clients ausreichen, da sie ihre einzige gemeinsame Grundlage ist.
- **04.01.2027 – M4, Prototyp:** Ausgewählte Funktionen in vorführbaren Vorversionen von Server und beiden Clients zeigen. Unser eigenes Ziel: Zwei Spieler können sich anmelden, zusammenfinden und mindestens eine Spielaktion über den Server austauschen, möglichst schon zwischen JavaFX- und Web-Client. Vollständige Integration ist laut Unterlagen zu diesem Termin noch nicht nötig.
- **Finale Abgabe und Präsentation – Termin offen:** Vollständiges Projekt abgeben und gemeinsam erklären.

Für die finale Abgabe prüfen wir:

- [ ] Alle geforderten Funktionen laufen in beiden Clients, einschließlich einer vollständigen Partie zwischen JavaFX- und Web-Client.
- [ ] Ergebnisse, Auswertungen und Profilbilder funktionieren über Server und Datenbank.
- [ ] Ungültige Aktionen und Verbindungsabbrüche werden sinnvoll behandelt.
- [ ] Build, Konfiguration und Start von Server und beiden Clients sind dokumentiert; die Abgabe ist in der Versionsverwaltung eindeutig erkennbar.
- [ ] Diagramme und Schnittstellenbeschreibung passen zum fertigen Stand.
- [ ] Alle drei können die Architektur, ihre eigenen Beiträge und die eingesetzten Patterns erklären.
- [ ] Präsentation und Live-Demo sind geprobt.

Bewertet werden unter anderem Lauffähigkeit, Anforderungserfüllung, Codequalität, Architektur, Frameworks, Patterns, Kommunikation und Präsentation. Die Vorleistungen sind unbenotet. Laut Leitfaden kann jedes Teammitglied die eigene Präsentation auf seine Schwerpunkte ausrichten, etwa auf den eigenen Client. Die Prüfungsdauer beträgt je Gruppenmitglied rund 30 Minuten Präsentationszeit zzgl. Fragen und Diskussionen.

## Zusammenarbeit und nächste Schritte

Jede Person übernimmt eine Komponente als Schwerpunkt:

- **Server und Datenbank:** _Name offen_
- **JavaFX-Client:** _Name offen_
- **Vue.js-Client:** _Name offen_

Datenmodell, Schnittstellen, Integration und gegenseitige Reviews machen wir gemeinsam. Alle drei sollen den Gesamtaufbau verstehen. Die beiden Clients entstehen getrennt; Code wird zwischen ihnen nicht übernommen. Einen regelmäßigen Abstimmungstermin tragen wir noch ein.

Das Repository liegt künftig im GitLab der Hochschule.

Als Nächstes:

- [ ] Namen und Schwerpunkte eintragen.
- [ ] Offene Spielregeln durchgehen und eine Beispielpartie auf Papier spielen.
- [ ] Übrige Werkzeuge festlegen.
- [ ] Repository ins Hochschul-GitLab laden, sobald der Zugang da ist.
- [ ] Startbare Projektstruktur für Server und beide Clients einrichten.
- [ ] Datenmodell und Schnittstellen gemeinsam entwerfen.
- [ ] Früh in beiden Clients einen vollständigen Weg von der Codeeingabe bis zur Serverantwort bauen.
