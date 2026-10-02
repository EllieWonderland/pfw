# Rahmenplan – Patterns and Frameworks

Stand: 02.10.2026 · Wintersemester 2026/27

## Unser Projekt

Wir entwickeln zu dritt eine Abwandlung des Brettspielklassikers 'Mastermind' als Online-Multiplayer-Spiel.

Wir bauen einen Java-Server, eine relationale Datenbank und zwei grafische Clients: einen Desktop-Client mit JavaFX und einen Web-Client als Vue.js-SPA. Beide Clients nutzen dieselben Hochschulserver-Schnittstellen und bieten jeweils den vollen Funktionsumfang mit beiden Spielrollen. Deshalb können auch zwei Personen mit unterschiedlichen Clients gegeneinander spielen.

Unser Ziel ist eine vollständige, gut erklärbare Anwendung mit überschaubarem Spielumfang. Neben dem Spiel brauchen wir Zeit für Konten, Spielersuche, Datenbank, Schnittstellen und die Abgaben. Da jede Funktion in zwei Clients umgesetzt wird, halten wir den Spielumfang bewusst klein.

## Spielidee: 'Mastermind: Cipher Chase'

Ein Dieb flieht durch ein Gebäude, ein Polizist verfolgt ihn. Um Türen zu öffnen, knacken beide Zahlencodes nach dem Mastermind-Prinzip. Der Dieb hinterlässt seine bisherigen Eingaben als Spuren. Der Polizist entscheidet, ob er Zeit in diese Hinweise investiert oder selbst rätselt.

### Grundregeln

- Die Rollen werden zufällig vergeben. Sobald zwei Spieler in der Warteschlange stehen, startet die Partie automatisch.
- Es gibt insgesamt sechs Türen. Jeder Raum endet mit einer Tür: Raum 1 führt über Tür 1 zu Raum 2, Raum 2 über Tür 2 zu Raum 3 und so weiter.
- Jeder Code besteht aus vier Zahlen zwischen 1 und 6. Zahlen dürfen mehrfach vorkommen.
- Grün bedeutet: richtige Zahl an der richtigen Position. Orange bedeutet: richtige Zahl an der falschen Position.
- Erst bei vier grünen Treffern öffnet sich die Tür.
- Die Figur geht durch die Tür und weiter zur nächsten. Die Tür hinter ihr schließt sich automatisch. Für diese gesamte Szene sind kurze Videosequenzen vorgesehen, keine eigenen Bewegungsabläufe.
- Der Dieb hat einen Vorsprung über einen Raum beziehungsweise zwei Türen. Die genaue Aufstellung legen wir noch fest.
- Der Dieb gewinnt, wenn er die letzte Tür vor Ablauf des Countdowns passiert.
- Läuft der Countdown vorher ab, stürmt das SEK das Gebäude und die Polizei gewinnt.
- Die Polizei gewinnt auch, sobald sie nach einer Türöffnung im selben Raum steht wie der Dieb. Öffnen beide fast gleichzeitig eine Tür, gilt die Reihenfolge, in der der Server die Eingaben verarbeitet.
- Keine Rolle weiß, was die andere gerade tut. Der Polizist sieht weder Position noch aktuelle Eingaben des Diebs, der Dieb nicht die des Polizisten. Der Polizist erfährt nur über die Spuren etwas über die bisherigen Versuche des Diebs.

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

### Gadgets (Ausblick V1, nicht Teil der Abgabe)

Gadgets gehören nicht zum MVP. Sie sind erst für eine V1 vorgesehen, die nicht mehr zur Abgabe gehört. Deshalb kommen sie in Klassen- und Komponentendiagramm nicht vor. Die folgende Beschreibung halten wir nur als Idee fest.

Jede Rolle hat zwei Gadgets. Jedes Gadget lässt sich einmal pro Partie einsetzen.

- **Dietrich für den Dieb:** Deckt eine Zahl des Codes mit ihrer Position auf.
- **Rauchbombe für den Dieb:** Entfernt alle Spuren an der Tür, durch die der Dieb zuletzt gegangen ist. Die Polizei kann dort keine Eingabehistorie mehr abrufen.
- **Alarm für den Polizisten:** Ändert eine zufällige Stelle des Codes, an dem der Dieb gerade arbeitet.
- **Fingerabdruck für den Polizisten:** Deckt eine im Code enthaltene Zahl ohne Position auf.

Nach einem Alarm wird klar angezeigt, dass bisherige Versuche zum alten Code gehören. Bereits aufgedeckte Hinweise von Dietrich oder Fingerabdruck bleiben sichtbar, werden aber ebenfalls als veraltet markiert. Versuche nach dem Alarm beziehen sich auf den neuen Code und werden normal als Spuren hinterlassen.

Vor einer Umsetzung wären noch zu klären:

- **Alarm:** Muss der Polizist an dieser Tür später den neuen Code lösen? Erfährt jemand, welche Stelle sich geändert hat? Drei von vier Stellen bleiben gleich, die alten Spuren sind also teilweise noch brauchbar. Im Laufzeitmodell hat jede Tür bisher genau einen unveränderlichen Code; das müsste dafür angepasst werden.
- **Fingerabdruck:** Gilt er für den Code, an dem der Polizist gerade arbeitet, oder für den Code, an dem der Dieb gerade arbeitet?
- **Dietrich:** Sieht der Polizist den vom Dieb aufgedeckten Hinweis später an derselben Tür?

### Festgelegte MVP-Regeln und verbleibende Balancefragen

Die verbindlichen Festlegungen stehen in [team-und-schnittstellen.md](team-und-schnittstellen.md): Polizei startet vor Tür 1, Dieb vor Tür 3; zunächst 240 Sekunden Spielzeit, kein Versuchslimit und keine zusätzliche Eingabepause. Spuren sind sofort nach Auswertung verfügbar, werden chronologisch einzeln freigeschaltet und dürfen nach dem Lesen erneut angesehen werden. Der Server prüft den Zeitablauf vor jeder Aktion und verarbeitet die Ereignisse geordnet.

Bewusstes Verlassen zählt als Niederlage. Bei Verbindungsverlust läuft der Countdown weiter; nach serverseitiger Erkennung bleiben 30 Sekunden für die Wiederverbindung. Danach wird eine noch laufende Partie ohne Gewinner abgebrochen. Ein vorheriges reguläres Spielende hat Vorrang.

In Testpartien prüfen wir noch, ob Startvorsprung, Spielzeit und Zwei-Sekunden-Sperre ausgewogen sind. Änderungen werden gemeinsam beschlossen und in der Schnittstellenreferenz festgehalten.

### Umfang und erste Tests

Zuerst bauen wir Codeeingabe und Auswertung, sechs Türen, Vorsprung, Countdown, Spuren und die Fangregel. Für die Übergänge reicht am Anfang eine kurze Türanimation; Videos kommen später.

Wir testen mit vertauschten Rollen und mit beiden Clients, ob der Polizist den Vorsprung aufholen kann und ob sich das Anschauen der Spuren lohnt. Countdown und Wartezeiten passen wir anhand dieser Partien an. Gadgets gehören nicht zu diesem Umfang.

Die Idee passt gut zu unserem Projekt: Die Rätselregeln bleiben überschaubar, während die Verfolgung und die Spuren für Interaktion sorgen. Das größte offene Thema ist die Spielbalance.

## Was die fertige Anwendung können muss

- Zwei angemeldete Personen können eine vollständige Partie spielen, auch wenn eine den JavaFX-Client und die andere den Web-Client nutzt.
- Registrierung mit Benutzername und Passwort, Login und Logout funktionieren.
- Spieler finden sich über eine Warteschlange. Sobald zwei Personen warten, startet der Server die Partie automatisch und lost die Rollen aus.
- Abgeschlossene Partien werden gespeichert. Jeder sieht seine Spielhistorie und einfache Auswertungen: Siege und Niederlagen je Rolle, Zeit und Anzahl der Versuche. Einen zusätzlichen Punktestand gibt es nicht. Diese Spielhistorie ist getrennt von den Eingabespuren innerhalb einer Partie.
- Profilbilder (hochladen, abrufen) decken den Punkt der sinnvollen Übertragung über die API und Anzeige im Client ab.
- Alle Funktionen sind in beiden Clients über die grafische Oberfläche bedienbar.

## Technischer Plan

### Grundlagen

- Server und die beiden Clients sind getrennte Komponenten.
- Der Server wird in Java umgesetzt.
- Benutzer, Profilbilder und Spielergebnisse liegen in einer relationalen Datenbank und werden über ein ORM verwaltet. Das Datenbankprodukt ist noch offen.
- Die API unterstützt JWT; die allgemeine Prüfungsbeschreibung verlangt diese Unterstützung.
- Die Server-Schnittstellen sind clientneutral: Beide Clients nutzen dieselben Endpunkte und Nachrichten, ohne Sonderwege für einen der beiden.
- Wir setzen Entwurfs- und Architekturmuster dort ein, wo sie uns helfen, und können ihren Nutzen erklären.
- Synchrone und asynchrone Kommunikation sowie sinnvolle Parallelverarbeitung gehören zum Projekt. Netzwerkzugriffe dürfen die Oberfläche nicht blockieren.

### Geplante Werkzeuge

Spring Boot ist für den Server laut Leitfaden Pflicht. Dazu orientieren wir uns am Leitfaden: Spring Security mit JWT, JPA, Git und Maven. REST mit JSON verwenden wir für Konten, Profilbilder und Spielhistorie; WebSocket mit STOMP für Warteschlange, Spielaktionen und Spuren. Beide Wege laufen verschlüsselt über HTTPS beziehungsweise WSS. Die Versionen legen wir beim Projektstart fest.

Clients:

- **Desktop-Client:** Java mit JavaFX.
- **Web-Client:** JavaScript mit Vue.js, umgesetzt als Single Page Application.

Beide Clients müssen mit unterschiedlichen Sprachen oder Frameworks und vollständig unabhängig voneinander entwickelt werden. Laut Leitfaden darf ein Client nicht durch Migration aus dem anderen entstehen. Gemeinsame Grundlage ist nur die dokumentierte Server-Schnittstelle. Für den Web-Client müssen wir zusätzlich den Zugriff aus dem Browser (CORS), die Ablage des JWT und den STOMP-Zugang über JavaScript einplanen.

### Zuständigkeiten im Spiel

Der Server verwaltet Warteschlange, Codes, Countdown, Positionen und Versuche. Er prüft Aktionen und entscheidet über Türöffnung, Einholen und Spielende. Jeder Client bekommt nur die Informationen, die seine Rolle sehen darf; geheime Codes werden nicht vollständig an die Clients geschickt. Die Clients stellen den Zustand nur dar und schicken Aktionen; Spiellogik doppeln wir dort nicht. Das hält auch die beiden Client-Implementierungen schlank.

Mögliche Ansatzpunkte für Patterns sind Spielphasen und Spielaktionen auf dem Server sowie die Trennung von Darstellung und Zustand in den Clients. Die konkrete Auswahl treffen wir bei der Modellierung.

### Technische Festlegungen und verbleibende Werkzeugfragen

Nachrichtenformate, JWT über STOMP, Zeitsynchronisation, Mehrfachanmeldung und Spurenfreigabe sind in [team-und-schnittstellen.md](team-und-schnittstellen.md) festgelegt. Der Server liefert Spuren einzeln und speichert den Lesefortschritt samt Freigabezeit. Die Clients berechnen den Countdown aus Endzeitpunkt und Serverzeit und korrigieren ihre Anzeige regelmäßig. Pro Konto ist nur eine aktive STOMP-Verbindung erlaubt.

Noch festzulegen sind das Datenbankprodukt, die Frameworkversionen und die Medienproduktion. Für das erste spielbare MVP genügt eine kurze Türanimation. Falls später Videos ergänzt werden: Zuständigkeit, Auslieferung und ein in JavaFX und Browser getestetes Format gemeinsam festlegen.

## Termine und Abgaben

Die Termine kommen aus dem Kursplan. Die Arbeitsschritte darunter sind unsere interne Planung. Die Videochats (jeweils 19:00 Uhr) nutzen wir für Rückfragen und Reviews: 07.10., 21.10., 04.11., 18.11., 02.12., 16.12.2026 und 13.01.2027.

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

Die verbindliche Rollenaufteilung, MVP-Regeln und REST-/STOMP-Schnittstellen stehen in [team-und-schnittstellen.md](team-und-schnittstellen.md). Diese Referenz ist für die Implementierung maßgeblich; Änderungen stimmen wir gemeinsam ab und halten sie dort fest.

Jede Person übernimmt eine Komponente als Schwerpunkt:

- **Server und Datenbank:** Manu
- **JavaFX-Client:** Ti
- **Vue.js-Client:** Jana

Datenmodell, Schnittstellen, Integration und gegenseitige Reviews machen wir gemeinsam. Alle drei sollen den Gesamtaufbau verstehen. Die beiden Clients entstehen getrennt; Code wird zwischen ihnen nicht übernommen. Einen regelmäßigen Abstimmungstermin tragen wir noch ein.

Das Repository liegt künftig im GitLab der Hochschule.

Als Nächstes:

- [x] Namen und Schwerpunkte eintragen.
- [ ] Offene Spielregeln durchgehen und eine Beispielpartie auf Papier spielen.
- [ ] Übrige Werkzeuge festlegen.
- [ ] Repository ins Hochschul-GitLab laden, sobald der Zugang da ist.
- [ ] Startbare Projektstruktur für Server und beide Clients einrichten.
- [ ] Datenmodell und Schnittstellen gemeinsam entwerfen.
- [ ] Früh in beiden Clients einen vollständigen Weg von der Codeeingabe bis zur Serverantwort bauen.
