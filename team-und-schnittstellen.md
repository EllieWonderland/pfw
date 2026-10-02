# Cipher Chase – Teamaufteilung und MVP-Schnittstellen

Stand: 02.10.2026 · Vertragsversion: 1

Dieses Dokument ist die verbindliche gemeinsame Grundlage für Server, JavaFX-Client und Vue-Client. Änderungen werden gemeinsam abgestimmt und hier festgehalten, bevor eine Komponente davon abweicht.

## 1. Umfang und Zuständigkeiten

Das MVP umfasst Konten, Profilbilder, automatische Spielersuche, beide Rollen, sechs Türen, Codeeingaben, Countdown, Spuren, Fangregel, gespeicherte Ergebnisse und eigene Statistiken. Beide Clients haben den vollständigen Funktionsumfang und können miteinander spielen.

**Gadgets gehören weder zum MVP noch zur Abgabe.** Es gibt keine Gadget-Aufträge oder -Felder, keine Codeänderungen, Codeversionen oder veralteten Spuren. Jede Tür hat während der gesamten Partie einen festen Code. Der V1-Ausblick im Rahmenplan ist keine Implementierungsanforderung.

| Person | Verantwortung | Ergebnis |
|---|---|---|
| **Manu** | Java-Server mit Spring Boot, Datenbank/ORM, Authentifizierung, REST/STOMP, Warteschlange, autoritative Spielregeln, persönliche Zustände, Ergebnisablage | Startbarer Server, Regel-/Schnittstellentests, Datenbankkonfiguration und Serverdokumentation |
| **Ti** | JavaFX: Anmeldung, Profil, Spielersuche, beide Rollen, Spielansicht, Spuren, Ergebnisse, Statistik und Verbindungsbehandlung | Vollständiger Desktop-Client, asynchrone Netzwerkzugriffe, Clientprüfungen und Build-/Startanleitung |
| **Jana** | Vue-SPA: derselbe Funktionsumfang, Browserdarstellung, Tokenverwaltung und REST/STOMP-Anbindung | Vollständiger Web-Client, Clientprüfungen und Build-/Startanleitung |

Jeder dokumentiert und präsentiert seinen Bereich und behebt dessen Fehler. Datenmodell, Schnittstellenänderungen, Balance, Reviews und Integration verantworten alle drei gemeinsam. Jana koordiniert Bedienkonzept und gemeinsame Medien; Ti koordiniert die Testfälle für Partien zwischen beiden Clients. Koordination bedeutet nicht, diese Arbeiten allein zu erledigen.

Die Clients entstehen unabhängig, ohne Migration oder Übernahme von Clientcode. Gemeinsame Grundlage sind dieser Vertrag, JSON-Beispiele und gegebenenfalls Medien. Beide können zunächst mit simulierten Serverantworten arbeiten.

Wöchentlich vergleichen wir die verbleibende Arbeit. Bei Überlastung von Manu können Ti oder Jana abgestimmte, abgegrenzte Serveraufgaben wie Profilbilder oder Ergebnisabfragen übernehmen. Die zentrale Spielverwaltung bleibt bei Manu.

## 2. Festgelegte Regeln für die erste Umsetzung

Die folgenden Festlegungen schließen bisher offene Fragen für die erste Umsetzung. Balancewerte können nach gemeinsamen Testpartien geändert werden; beide Clients erhalten relevante Werte über den Server.

- Sechs Türen; jeder Code hat vier Zahlen von 1 bis 6, Wiederholungen erlaubt. Beide Rollen lösen an derselben Tür denselben Code.
- Polizei startet in Raum 1 vor Tür 1; Dieb in Raum 3 vor Tür 3, also zwei Türen Vorsprung. Die ersten beiden Türen haben keine künstlichen Spuren.
- Zunächst **240 Sekunden** Spielzeit, kein Versuchslimit und keine spielregelbedingte Pause zwischen eigenen Eingaben. Technische Begrenzungen gegen Nachrichtenfluten sind davon getrennt.
- Zwei wartende Spieler werden automatisch zusammengeführt, Rollen zufällig vergeben und die Partie gestartet. Keine manuelle Lobby, Rollenwahl oder Bereitschaftsbestätigung.
- Vier grüne Treffer öffnen die Tür sofort auf dem Server. Eine Clientanimation verändert weder Position noch Zeit. Durch Tür 6 entkommt der Dieb.
- Grün zählt exakte Treffer, Orange danach passende übrige Zahlen. Keine Zahl wird doppelt gewertet. Die Marker zeigen keine einzelnen Positionen.
- Nach einer Türöffnung prüft der Server auf Einholen: Stehen beide im selben Raum, gewinnt die Polizei. Der Austritt des Diebs durch Tür 6 ist bereits ein Spielende und wird nicht nachträglich als Einholen gewertet.
- Vor jeder Spielaktion prüft der Server den Endzeitpunkt. Bei oder nach diesem Zeitpunkt gewinnt die Polizei durch Zeitablauf. Vorher verarbeitete Aktionen können noch das Spiel entscheiden. Aktionen und Timerentscheidungen werden pro Partie geordnet verarbeitet; eine Partie endet genau einmal.
- Fehlversuche des Diebs werden sofort nach Auswertung als Spuren verfügbar, nur an der aktuellen Tür der Polizei, vom ältesten zum neuesten. Erfolgreiche Versuche sind niemals Spuren.
- Bewusstes Verlassen zählt als Niederlage. Bei Verbindungsverlust läuft die Zeit weiter; **30 Sekunden Reconnect-Frist** ab serverseitiger Erkennung. Danach wird eine noch laufende Partie ohne Gewinner abgebrochen. Ein vorher eintretendes reguläres Spielende hat Vorrang, auch wenn beide Spieler getrennt sind.

## 3. Kommunikationsgrundlagen

- REST-Basis `/api/v1`, JSON in UTF-8, HTTPS. WebSocket-Endpunkt `/ws`, STOMP über WSS; kein vorausgesetzter SockJS-Fallback.
- IDs sind undurchsichtige Strings. Zeitpunkte: ISO 8601 in UTC mit `Z`. Dauerfelder benennen ihre Einheit.
- REST nutzt `Authorization: Bearer <JWT>`. STOMP überträgt denselben Header im `CONNECT`-Frame, keinen Token in der URL. Der Server prüft Authentifizierung und erlaubte SEND-/SUBSCRIBE-Ziele.
- Login liefert eine serverseitig widerrufbare Sitzung mit JWT und Ablaufzeit. Anfangswert: acht Stunden gültig, kein Refresh-Endpunkt im MVP. Nach Ablauf ist erneuter Login erforderlich; die Reconnect-Frist läuft weiter. Bei Tokenablauf schließt der Server die STOMP-Verbindung mit `AUTH_EXPIRED`.
- Logout widerruft die Sitzung, entfernt das Konto aus der Warteschlange und schließt dessen Verbindungen. Während einer Partie zählt Logout als bewusstes Verlassen.
- Mehrere REST-Sitzungen sind erlaubt, aber nur eine aktive STOMP-Verbindung je Konto. Eine zweite wird mit `CONNECTION_ALREADY_ACTIVE` abgelehnt und übernimmt die erste nicht. Ein Konto darf höchstens einmal warten oder in einer laufenden Partie sein.
- Jana hält den JWT im Arbeitsspeicher der SPA; nach Neuladen ist Login nötig. Ti hält ihn für die Anwendungssitzung im Speicher. Manu richtet CORS für die vereinbarte Web-Origin ein.
- STOMP-Heartbeats: 10 Sekunden in beide Richtungen. Spätestens nach 30 Sekunden ohne erwartetes Lebenszeichen gilt die Verbindung serverseitig als ausgefallen; dann beginnt die zusätzliche Reconnect-Frist.
- Identität und Rolle stammen aus Sitzung und Partie. Clientangaben über Rolle, Position oder Zeit sind keine Entscheidungsgrundlage.

## 4. REST-Endpunkte

Außer Registrierung und Login benötigen alle Endpunkte eine gültige Sitzung. Keine Antwort enthält Passwörter oder Passwort-Hashes.

| Methode und Pfad | Eingabe | Erfolgsantwort |
|---|---|---|
| `POST /auth/register` | `{ "username": "jana", "password": "…" }` | `201`, `{ "userId": "u-1", "username": "jana" }`; danach Login |
| `POST /auth/login` | Benutzername und Passwort | `200`, `{ "token": "…", "expiresAt": "2026-10-03T00:00:00Z", "user": { "userId": "u-1", "username": "jana", "avatarUrl": null } }` |
| `POST /auth/logout` | Kein Body | `204` |
| `GET /users/me` | – | `200`, `{ "userId": "u-1", "username": "jana", "avatarUrl": null }` |
| `PUT /users/me/avatar` | Multipart-Feld `file`: PNG/JPEG, maximal 2 MiB | `200`, `{ "avatarUrl": "/api/v1/users/u-1/avatar" }` |
| `GET /users/{userId}/avatar` | – | `200`, Bildbytes mit passendem Content-Type; `404`, wenn kein Bild vorhanden |
| `GET /users/me/games?page=0&size=20` | Seite ab 0, Größe 1–100 | `200`, `{ "items": [GameResult], "page": 0, "size": 20, "totalElements": 1 }`, neueste zuerst |
| `GET /users/me/games/{gameId}` | Nur eigene abgeschlossene Partie | `200`, `GameResult` |
| `GET /users/me/statistics` | – | `200`, Statistik pro Rolle |

Benutzername: 3–32 Zeichen aus A–Z/a–z, Zahlen oder Unterstrich; eindeutig ohne Beachtung der Groß-/Kleinschreibung. Passwort: 8–128 Zeichen. Der Server prüft Bildformat und Größe. Beide Clients laden geschützte Bildbytes mit Authentifizierung; bei `avatarUrl: null` erscheint ein Platzhalter.

`GameResult` enthält `gameId`, `startedAt`, `finishedAt`, `durationSeconds`, `yourRole`, `opponent` (nur `userId`, `username`), `winnerRole`, `reason`, `yourAttemptCount` und `outcome`. `outcome`: `WIN`, `LOSS` oder `ABORTED`; `winnerRole`: `THIEF`, `POLICE` oder `null`. Diese REST-Antworten enthalten keine Codes oder Eingabespuren. Abschnitt 7 zeigt ein vollständiges Ergebnisbeispiel.

Statistikformat:

```json
{
  "roles": {
    "THIEF": { "wins": 0, "losses": 0, "aborted": 0, "totalDurationSeconds": 0, "totalAttempts": 0 },
    "POLICE": { "wins": 0, "losses": 0, "aborted": 0, "totalDurationSeconds": 0, "totalAttempts": 0 }
  }
}
```

Abbrüche werden getrennt von Siegen/Niederlagen gezählt. Gesamtzeit und Gesamtversuche enthalten auch abgebrochene Partien.

## 5. STOMP-Aufträge und Nachrichten

Jeder Auftrag trägt eine `requestId`; Spielaufträge zusätzlich `gameId`. Neue fachliche Aktionen verwenden neue IDs, identische Transportwiederholungen dieselbe ID und denselben Inhalt.

| Client-Ziel | Weitere Felder | Wirkung |
|---|---|---|
| `/app/queue/join` | Keine | Warten; gegebenenfalls automatische Partie starten |
| `/app/queue/leave` | Keine | Warteschlange verlassen; eine schon gestartete Partie bleibt bestehen |
| `/app/queue/sync` | Keine | Wartestatus oder Verweis auf laufende Partie abrufen |
| `/app/game/guess` | `door`: erwartete eigene Tür; `digits`: vier ganze Zahlen | Auswerten, Spurenansicht schließen, gegebenenfalls Tür öffnen/Spiel beenden |
| `/app/game/traces/open` | `door` | Nur Polizei: Spurenansicht öffnen |
| `/app/game/traces/next` | `door` | Nur Polizei: nächste ungelesene Spur anfordern |
| `/app/game/traces/close` | `door` | Nur Polizei: Spurenansicht schließen |
| `/app/game/sync` | Keine | Persönlichen Zustand oder Endergebnis abrufen |
| `/app/game/leave` | Keine | Bewusst verlassen |

Beispielversuch:

```json
{
  "requestId": "req-123",
  "gameId": "game-42",
  "door": 2,
  "digits": [1, 3, 3, 6]
}
```

`door` verhindert die Ausführung eines verspäteten Auftrags an der nächsten Tür. Der Client lässt nur einen unbestätigten Versuch gleichzeitig zu. Der Server merkt sich Auftrags-IDs und Ergebnisse je Konto für die aktive Sitzung und Partie einschließlich Reconnect-Frist. Identische Wiederholungen liefern das ursprüngliche Ergebnis ohne erneute Wirkung; gleiche ID mit anderem Inhalt wird abgelehnt. Dies gilt auch für Spurenabrufe und erfolglos beantwortete Aufträge.

Der Client abonniert nach CONNECT zuerst alle persönlichen Queues und fordert dann `queue/sync` an. Bei laufender Partie folgt `game/sync`. Kein öffentliches Spiel-Topic und keine öffentliche Wartendenliste.

| Subscription | Typ | Inhalt |
|---|---|---|
| `/user/queue/queue` | `QUEUE_STATE` | `{ "type": "QUEUE_STATE", "status": "IDLE" }`; alternativ `WAITING` oder `IN_GAME`, bei letzterem zusätzlich `gameId` |
| `/user/queue/game` | `GAME_STATE`, `GAME_FINISHED` | Persönlicher Zustand oder Endergebnis |
| `/user/queue/results` | `ACTION_RESULT` | Annahme/Ablehnung mit Auftrags-ID |

Bei Matchbeginn: `QUEUE_STATE` mit `IN_GAME` und persönlicher `GAME_STATE` für beide. Jeder Auftrag erhält ein `ACTION_RESULT`; Zustandsänderungen zusätzlich einen persönlichen `GAME_STATE`. Clients verlassen sich nicht auf die Reihenfolge zwischen verschiedenen Queues.

Ergebnis eines erfolgreichen Versuchs:

```json
{
  "type": "ACTION_RESULT",
  "requestId": "req-123",
  "accepted": true,
  "data": {
    "door": 2,
    "attempt": { "attemptId": "a-7", "digits": [1, 3, 3, 6], "green": 1, "orange": 2 },
    "doorOpened": false
  }
}
```

Bei anderen erfolgreichen Aufträgen entfällt `data`. Zustände werden nach eigenen Aktionen, beim Start, bei Synchronisation und alle 15 Sekunden zur Zeitkorrektur gesendet. Geheime gegnerische Aktionen lösen keine zusätzlichen Updates aus. Spielende und Wechsel der Verbindungsverfügbarkeit werden unmittelbar gemeldet.

## 6. Sichtbarkeit und Beispielzustand

| Information | Dieb | Polizei |
|---|---|---|
| Eigene Rolle, Position und Versuche | Ja | Ja |
| Vollständige geheime Codes | Nein | Nein |
| Gegnerposition und aktuelle Gegnereingaben | Nein | Nein |
| Name und Verbindungsverfügbarkeit des Gegners | Ja | Ja |
| Endzeitpunkt und Serverzeit | Ja | Ja |
| Fehlversuche des Diebs an der eigenen Tür | Eigene Versuche | Nur einzeln freigeschaltete Spuren |
| Erfolgreiche Diebeingabe | Eigene Eingabe | Keine Spur |
| Endergebnis | Ja | Ja |

Polizeiansicht; `delivered` enthält ausschließlich bereits freigeschaltete Spuren:

```json
{
  "type": "GAME_STATE",
  "gameId": "game-42",
  "viewVersion": 12,
  "serverTime": "2026-10-02T18:00:20Z",
  "status": "RUNNING",
  "startedAt": "2026-10-02T18:00:00Z",
  "endsAt": "2026-10-02T18:04:00Z",
  "opponent": { "userId": "u-2", "username": "manu", "connected": true },
  "you": {
    "role": "POLICE",
    "room": 2,
    "door": 2,
    "attemptCount": 7,
    "currentDoorAttempts": [
      { "attemptId": "a-7", "digits": [1, 3, 3, 6], "green": 1, "orange": 2 }
    ],
    "traceView": {
      "open": true,
      "delivered": [
        { "attemptId": "t-3", "digits": [2, 3, 4, 5], "green": 0, "orange": 2 }
      ],
      "nextAllowedAt": "2026-10-02T18:00:22Z"
    }
  }
}
```

`viewVersion` steigt je Spieler und Partie bei jedem Versand; sie zählt keine verborgenen Spieländerungen. Clients ignorieren ältere Ansichten. `attemptCount` zählt alle eigenen Versuche, `currentDoorAttempts` nur die aktuelle Tür. Die Auswertung einer erfolgreichen Eingabe bleibt im `ACTION_RESULT`, auch wenn der Zustand bereits die nächste Tür zeigt.

Für den Dieb entfällt `traceView`. Ungelesene Spuren und deren Gesamtzahl werden nie vorab geliefert. Beim eigenen Türwechsel schließen sich Spurenansicht und bisherige Türansicht; eigene Versuche und sichtbare Spuren beziehen sich danach auf die neue Tür. Bereits bekannte Spuren dürfen bei geschlossener Ansicht im Client verbleiben.

## 7. Fehler, Spielende und Verbindungsabbruch

REST-Fehler: `{ "error": { "code": "…", "message": "…", "retryable": false } }`. STOMP nutzt denselben Fehlerblock:

```json
{
  "type": "ACTION_RESULT",
  "requestId": "req-124",
  "accepted": false,
  "error": {
    "code": "TRACE_WAIT_ACTIVE",
    "message": "Die nächste Spur ist noch gesperrt.",
    "retryable": true,
    "retryAt": "2026-10-02T18:00:22Z"
  }
}
```

| Code | Bedeutung | REST-Status, falls zutreffend |
|---|---|---|
| `INVALID_INPUT` | Ungültige Felder/Werte | 400 |
| `USERNAME_TAKEN` | Benutzername vergeben | 409 |
| `AUTH_INVALID` / `AUTH_EXPIRED` | Ungültige/abgelaufene Sitzung | 401 |
| `FORBIDDEN` | Aktion für Rolle nicht erlaubt | 403 |
| `NOT_FOUND` | Ressource fehlt oder Partie gehört nicht zum Konto | 404 |
| `ALREADY_IN_GAME` / `CONNECTION_ALREADY_ACTIVE` | Bereits im Spiel / aktive Verbindung vorhanden | – |
| `GAME_NOT_RUNNING` | Partie nicht laufend | – |
| `DOOR_CHANGED` | Auftrag betrifft nicht mehr die eigene Tür | – |
| `TRACE_VIEW_CLOSED` | Erst Spurenansicht öffnen | – |
| `TRACE_WAIT_ACTIVE` | Wartezeit aktiv, mit `retryAt` | – |
| `NO_NEW_TRACE` | Keine weitere Spur vorhanden | – |
| `REQUEST_ID_CONFLICT` | ID mit anderem Inhalt wiederverwendet | – |
| `FILE_TOO_LARGE` / `UNSUPPORTED_IMAGE` | Bildgröße/-format ungültig | 413 / 415 |
| `RATE_LIMITED` | Technische Begrenzung, gegebenenfalls `retryAt` | 429 |
| `INTERNAL_ERROR` | Unerwarteter Fehler ohne interne Details | 500 |

`retryable` bedeutet, dass eine neue Aktion nach Beheben der Ursache sinnvoll ist. Sie erhält eine neue ID; eine identische Transportwiederholung behält die alte. Clients verwenden Fehlercodes, nicht Nachrichtentexte, für ihre Anzeige. Authentifizierungs-/Verbindungsfehler können als STOMP-ERROR mit Fehlerblock im JSON-Body und anschließendem Schließen gemeldet werden; auch ein CONNECT ohne akzeptierte Sitzung wird so abgelehnt.

Endnachricht mit persönlichem Ergebnis; REST liefert dieselben Ergebnisfelder ohne `type` und `viewVersion`:

```json
{
  "type": "GAME_FINISHED",
  "gameId": "game-42",
  "viewVersion": 30,
  "startedAt": "2026-10-02T18:00:00Z",
  "finishedAt": "2026-10-02T18:04:00Z",
  "durationSeconds": 240,
  "yourRole": "POLICE",
  "opponent": { "userId": "u-2", "username": "manu" },
  "winnerRole": "POLICE",
  "reason": "TIMEOUT",
  "yourAttemptCount": 18,
  "outcome": "WIN"
}
```

Endgründe: `ESCAPED`, `CAUGHT`, `TIMEOUT`, `PLAYER_LEFT`, `ABORTED_DISCONNECT` und `ABORTED_SERVER_RESTART`. Bei Verlassen gewinnt die andere Rolle; bei Abbruch gilt `winnerRole: null`, `outcome: ABORTED`. Ergebnis und Statistiken werden genau einmal gespeichert. `GAME_FINISHED` bestätigt die erfolgreiche Speicherung. Bei Speicherfehler bleibt die Partie beendet, der Server versucht die Speicherung erneut und sendet die Endnachricht erst danach. Ein `game/sync` währenddessen erhält `INTERNAL_ERROR` mit `retryable: true`, keine laufende Spielansicht.

Nach Ende gilt Wartestatus `IDLE`; kein automatischer Wiedereintritt. `game/sync` liefert für eine eigene beendete Partie erneut das Ergebnis, auch wenn die ursprüngliche Nachricht verpasst wurde.

Bei Verbindungsverlust sperrt der Client neue Aktionen und verbindet sich erneut. Erfolgreiche authentifizierte Wiederverbindung innerhalb der Frist stellt die Verbindung wieder her; danach abonnieren und synchronisieren. Bestätigte Aktionen werden nicht wieder ausgeführt. Der Gegner darf `connected: false` sehen, aber keine Position oder Eingaben.

Laufende Spielzustände liegen im MVP im Serverspeicher. Wiederaufnahme nach Serverneustart ist nicht Teil des Vertrags. Der Server speichert beim Start einen Partieeintrag mit Beteiligten und Startzeit und hält die bereits angenommenen Versuchszähler dauerhaft aktuell. Beim Neustart werden noch laufend markierte Partien ohne Gewinner mit `ABORTED_SERVER_RESTART` beendet. Codes und Spuren müssen dafür nicht dauerhaft gespeichert werden.

## 8. Countdown und Zwei-Sekunden-Sperre

Der Server legt beim Start einen festen Endzeitpunkt fest. Der Client berechnet die Differenz aus `endsAt` und `serverTime` und zählt mit einer monotonen lokalen Uhr herunter. Die lokale Kalenderuhr entscheidet nicht über die Restzeit. 15-Sekunden-Updates und `game/sync` korrigieren die Anzeige. Netzlaufzeit kann sie leicht verschieben, ändert aber nie den serverseitigen Ausgang. Bei angezeigter Null wartet der Client auf das bestätigte Ende.

Der Server verwaltet Lesefortschritt und `nextAllowedAt` pro Polizei, Partie und Tür:

1. Beim ersten Öffnen erscheint sofort die älteste vorhandene Fehlspur. Ohne Spur: leere Liste und `nextAllowedAt: null`.
2. Jede neu gelieferte Spur setzt `nextAllowedAt` auf serverseitigen Auslieferungszeitpunkt plus **2.000 Millisekunden**. Vorher wird `traces/next` abgelehnt.
3. Nach der Frist liefert `traces/next` genau eine weitere Spur. Ohne weitere Spur kommt `NO_NEW_TRACE`; dadurch startet keine neue Wartezeit. Neue Spuren werden nicht ungefragt geliefert.
4. Schon gelieferte Spuren darf der Client ohne Wartezeit erneut anzeigen.
5. Schließen, Öffnen und Reconnect setzen weder Lesefortschritt noch Wartezeit zurück. Wiederöffnen liefert nur bekannte Spuren; neue erfordern `traces/next`. War bisher keine Spur vorhanden, darf Öffnen die inzwischen vorhandene erste Spur sofort liefern.
6. Eigene Codeeingaben schließen die Ansicht. Zeit läuft weiter, kein zusätzlicher Zeitabzug.

Codes ändern sich im MVP nicht; alle Spuren bleiben gültig. Kein Vorladen ungelesener Spuren und keine rein lokale Sperre als Ersatz für Serverprüfung.

## 9. Gemeinsame Integrationsprüfung

Jeder prüft seine Komponente; diese Abläufe prüfen wir zusätzlich gemeinsam:

- Registrierung, Login, Profilbild, Logout und Sitzung abgelaufen in beiden Clients.
- Zwei unterschiedliche Clients werden automatisch gepaart und spielen vollständig; anschließend vertauschte Rollen.
- Auswertung mit Wiederholungszahlen; wiederholter Auftrag öffnet nicht mehrere Türen.
- Keine Codes, Erfolgsversuche als Spuren oder Gegnerpositionen in Netzwerkantworten.
- Spurenwartezeit bleibt nach Schließen/Reconnect wirksam; doppelte Aufträge überspringen keine Spur.
- Zeitablauf, Türöffnung und Einholen nahe am Endzeitpunkt ergeben genau ein Ergebnis.
- Reconnect innerhalb/außerhalb der Frist, bewusstes Verlassen, beide Verbindungen weg, Serverneustart und verpasste Endnachricht.
- Eigene Historie/Statistik stimmen einschließlich Abbrüchen; fremde Ergebnisse bleiben unzugänglich.

Konkretes Datenbankprodukt, Frameworkversionen und Videos bleiben separate Werkzeugentscheidungen. Für das erste spielbare MVP genügt eine kurze Türanimation; diese Entscheidungen ändern den Schnittstellenvertrag nicht.
