# Mastermind – To-dos

Stand: 27.10.2026

Grundlage sind [rahmenplan.md](rahmenplan.md) und [team-und-schnittstellen.md](team-und-schnittstellen.md). Diese Liste hält nur fest, wer was als Nächstes erledigt.

## Regeln

- Erledigte Aufgaben abhaken und mit dem Bearbeitungsdatum versehen. **Nicht löschen!**.
- Nach jeder erledigten Aufgabe wird in den eigenen Branch gepusht:

  | Person | Branch |
  |---|---|
  | Manu | `server` |
  | Ti | `javaclient` |
  | Jana | `vueclient` |

- Gemeinsame Aufgaben (Dokumente, Diagramme, Schnittstellenvertrag) landen auf `main`.
- Die drei Branches werden **jeden Sonntag** per Merge Request nach `main` zusammengeführt. Der Sonntag gilt vorläufig, bis ein fester Abstimmungstermin vereinbart ist.
- Zusätzlich wird sofort zusammengeführt, wenn der Server etwas fertig hat, das die Clients brauchen, und vor jedem Meilenstein.
- Auf `main` liegen nur startbare Stände.
- Jede Person holt `main` regelmäßig in den eigenen Branch, mindestens nach jeder Änderung am Schnittstellenvertrag.
- Aufgaben mit festem Termin tragen das Datum in Klammern.
- Hängt eine Aufgabe von jemand anderem ab, steht das dabei (`wartet auf: …`).

Beispiel:

```markdown
- [ ] Offene Aufgabe ABC (bis 19.10.2026)
- [x] Erledigte Aufgabe XYZ – erledigt 02.10.2026
```

## Gemeinsam (`main`)

### Meilensteine

- [x] M0 – Spielauswahl abstimmen (05.10.2026) – erledigt 02.10.2026
- [x] M1 – UML-Klassendiagramm (19.10.2026) – erledigt 19.10.2026
- [ ] M2 – UML-Komponentendiagramm (02.11.2026)
- [ ] M3 – Schnittstellenbeschreibung (16.11.2026)
- [ ] M4 – Prototyp von Server und beiden Clients (04.01.2027)
- [ ] Finale Abgabe und Präsentation (Termin offen)

### Organisation

- [ ] Repository ins Hochschul-GitLab laden, sobald der Zugang da ist
- [ ] Branches `server`, `javaclient` und `vueclient` anlegen
- [ ] Regelmäßigen Abstimmungstermin festlegen (bis dahin: sonntags)
- [ ] Datenbankprodukt und Frameworkversionen festlegen
- [ ] Datenbankprodukt und Versionen in [rahmenplan.md](rahmenplan.md) und [team-und-schnittstellen.md](team-und-schnittstellen.md) nachtragen (wartet auf: Datenbankprodukt und Frameworkversionen festlegen)
- [ ] Web-Origin für CORS festlegen
- [ ] HTTPS/WSS klären und einrichten (Zertifikat, Serverkonfiguration)
- [ ] Patterns auswählen und begründen (02.11.2026)
- [ ] Termin für finale Abgabe und Präsentation erfragen
- [ ] Beispielpartie auf Papier spielen
- [x] Schnittstellen in [team-und-schnittstellen.md](team-und-schnittstellen.md) von allen prüfen und gegebenenfalls anpassen (bis 25.10.2026) – erledigt 06.10.2026
- [ ] Jeden Sonntag: Branches nach `main` zusammenführen und verbleibende Arbeit vergleichen

### Integration und Abgabe

- [ ] Erster vollständiger Weg von der Codeeingabe bis zur Serverantwort in beiden Clients
- [ ] Integrationsprüfung nach Abschnitt 9 des Schnittstellenvertrags
- [ ] Testpartien mit vertauschten Rollen: Vorsprung, Spielzeit und Zwei-Sekunden-Sperre bewerten
- [ ] Diagramme und Schnittstellenbeschreibung an den fertigen Stand angleichen
- [ ] Präsentation und Live-Demo proben
- [ ] Abgabestand in Git eindeutig kennzeichnen (z. B. mit einem Tag)

## Manu – Server (`server`)

- [ ] Spring-Boot-Projekt mit Maven anlegen, startbar machen
- [x] Datenbankprodukt vorschlagen und JPA-Anbindung einrichten – erledigt 25.10.2026
- [ ] Fehler: Parameterlosen Konstruktor in `User` ergänzen (von JPA benötigt)
- [ ] Registrierung, Login, Logout mit JWT und widerrufbarer Sitzung
- [ ] Passwörter gehasht speichern
- [ ] CORS für die Web-Origin einrichten (wartet auf: Web-Origin festlegen)
- [ ] Profilbild hochladen und abrufen
- [ ] STOMP-Endpunkt `/ws` mit Authentifizierung im CONNECT-Frame
- [ ] Nur eine STOMP-Verbindung pro Konto (`CONNECTION_ALREADY_ACTIVE`), höchstens einmal wartend oder in einer Partie
- [ ] Warteschlange und automatisches Zusammenführen mit zufälligen Rollen
- [x] Codeauswertung (Grün/Orange, Wiederholungszahlen) mit Tests – erledigt 13.10.2026
- [ ] Fehler: `submitGuess` gibt nur Orange zurück, Grün fehlt
- [ ] Türöffnung, Fangregel, Countdown und Spielende
- [ ] Fehler: Timer in `GameSession` läuft im 240-Sekunden-Takt und zählt nur um 1 herunter, Zeitablauf greift praktisch nie
- [ ] Fehler: `GameSession` legt 7 statt 6 Türen an
- [ ] Fehler: Spielende kann mehrfach gemeldet werden (Partie muss genau einmal enden)
- [ ] Bewusstes Verlassen (`/app/game/leave`) und Logout während einer Partie als Niederlage werten
- [ ] Spuren mit Lesefortschritt und Zwei-Sekunden-Sperre
- [ ] Persönliche Zustände (`GAME_STATE`, `GAME_FINISHED`, `ACTION_RESULT`)
- [ ] Wiederholte Aufträge über `requestId` abfangen
- [ ] Einheitliches Fehlerformat und Fehlercodes nach Abschnitt 7 des Vertrags
- [ ] Technische Begrenzung gegen Nachrichtenfluten (`RATE_LIMITED`)
- [ ] Verbindungsabbruch, Reconnect-Frist und Abbruch bei Serverneustart
- [ ] Ergebnisse speichern, Spielhistorie und Statistik über REST
- [ ] Tests für Spielregeln und Schnittstellen
- [ ] Build-, Konfigurations- und Startanleitung für den Server

## Jana – Vue-Client (`vueclient`)

- [ ] Vue-Projekt als SPA anlegen, startbar machen
- [ ] Simulierte Serverantworten aus den JSON-Beispielen des Vertrags
- [ ] Registrierung, Login, Logout; JWT nur im Arbeitsspeicher
- [ ] Profil mit Profilbild (Upload, geschützter Abruf, Platzhalter)
- [ ] STOMP-Anbindung: verbinden, Queues abonnieren, `queue/sync`
- [ ] Spielersuche (Warteschlange beitreten/verlassen)
- [ ] Spielansicht für beide Rollen: Codeeingabe, Marker, eigene Versuche
- [ ] Countdown aus `endsAt` und `serverTime` mit monotoner Uhr
- [ ] Spurenansicht für die Polizei mit Wartezeit
- [ ] Ergebnisanzeige, Spielhistorie und Statistik
- [ ] Verbindungsverlust: Aktionen sperren, neu verbinden, synchronisieren
- [ ] Fehleranzeige anhand der Fehlercodes
- [ ] Partie verlassen sowie Logout und abgelaufene Sitzung (`AUTH_EXPIRED`) behandeln
- [ ] Kurze Türanimation
- [ ] Clientprüfungen (Tests)
- [ ] Build- und Startanleitung für den Web-Client
- [ ] Koordination: Bedienkonzept und gemeinsame Medien abstimmen

## Ti – JavaFX-Client (`javaclient`)

- [ ] JavaFX-Projekt mit Maven anlegen, startbar machen
- [ ] Simulierte Serverantworten aus den JSON-Beispielen des Vertrags
- [ ] Registrierung, Login, Logout; JWT für die Anwendungssitzung im Speicher
- [ ] Profil mit Profilbild (Upload, geschützter Abruf, Platzhalter)
- [ ] STOMP-Anbindung: verbinden, Queues abonnieren, `queue/sync`
- [ ] Netzwerkzugriffe asynchron, ohne die Oberfläche zu blockieren
- [ ] Spielersuche (Warteschlange beitreten/verlassen)
- [ ] Spielansicht für beide Rollen: Codeeingabe, Marker, eigene Versuche
- [ ] Countdown aus `endsAt` und `serverTime` mit monotoner Uhr
- [ ] Spurenansicht für die Polizei mit Wartezeit
- [ ] Ergebnisanzeige, Spielhistorie und Statistik
- [ ] Verbindungsverlust: Aktionen sperren, neu verbinden, synchronisieren
- [ ] Fehleranzeige anhand der Fehlercodes
- [ ] Partie verlassen sowie Logout und abgelaufene Sitzung (`AUTH_EXPIRED`) behandeln
- [ ] Kurze Türanimation
- [ ] Clientprüfungen (Tests)
- [ ] Build- und Startanleitung für den Desktop-Client
- [ ] Koordination: Testfälle für Partien zwischen beiden Clients

## Offene Fragen und Blocker

Hier steht, was jemanden aufhält oder gemeinsam entschieden werden muss. Geklärte Punkte werden wie Aufgaben abgehakt und datiert.

- [x] Wann und wie werden die drei Branches nach `main` zusammengeführt? – erledigt 02.10.2026: jeden Sonntag per Merge Request, siehe Regeln
