# Flight Booking

Lennureisijale lennu planeerimise ja lennukis istekohtade soovitamise veebirakendus.

[Selle](https://github.com/Neital/CGI-FlightBooking/tree/master) repositooriumi järeltulija.

# Sisukord
1. [Programmi käivitamine](#setup)
2. [Progress](#progress)
3. [Raskused](#struggles)
4. [Kasutatud materjalid](#materials)

## Programmi käivitamine <a name="setup"></a>

See kasutusjuhend on mõeldud Windowsi kasutajatele.

Repositooriumi kloonimiseks kasutage järgmine käsk:

``git clone https://github.com/Neital/FlightBooking-CGI.git``

või laadige .zip faili alla ja pakkige see lahti, kus iganes soovite.
Järgmised sammud on soovituslik teha IntelliJ-s.

Veenduge, et teil on paigaldatud kogu vajalik tarkvara projekti jaoks.
- Gradle
- Java 21
- Spring Boot
- Docker Desktop
- Node.js (npm)

Avage projekt IntelliJ-s ning käivitage järgmine käsk:

``gradle wrapper``

Kui soovite programmi käivitada lokaalselt, peate käivitama ka järgmised käsud:

``cd frontend``

``npm install``

``cd ..``

Kui mingil põhjusel build ebaõnnestus, parandage probleemid enne jätkamist.

### Programmi käivitamine lokaalselt

``.\start-local.bat``

### Programmi käivitamine Dockeriga

Docker Desktop peab olema käivitatud.

``.\start-docker.bat``

- Programmi lõpetamisel ``Ctrl+C`` ilmub ``End execution of batch file [Y(yes)/N(no)]?``, valige no (see käivitab docker-compose down). 

Backend on kättesaadav läbi http://localhost:8080/ ning frontend läbi http://localhost:3000/

## Progress <a name="progress"></a>

### Andmbebaas

Tehtud: 100%

Kulunud aeg: umbes 3-6 päeva (palju väikseid parandusi)

### Backend

Tehtud: ~95%

Kulunud aeg: 1.75 nädalat

Tegemata:
- ~~meetod, mis jaotab juhuslikult vabu istekohti lennul.~~
- meetod, mis võimaldab käsitsi määrata istekohtade klassi lennukis. (Oleks tore, kuid mitte vajalik)
- meetod, mis salvestab valitud kohad ja muudab need kättesaamatuks. (Tehniliselt ei ole nõutav vastavalt kirjeldusele)

### Frontend

Tehtud: ~95%

Kulunud aeg: 2 päeva

Tegemata:
- ~~meetod, mis soovitab istekohti vastavalt soovitustele.~~
- ~~komponent, mis annab valida piletid ja soovitused.~~
- istekoha valimise funktsionaalsus. (Tehniliselt ei ole nõutav vastavalt kirjeldusele)
- ~~lennuki ja istekohtade välimus.~~
- ~~veebirakenduse välimus.~~

## Raskused <a name="struggles"></a>

Kriitiline probleem oli see, et ma ei mõistnud Spring Boot'i. See oli minu esimene kord, kui ma seda kasutasin, 
nii et mul kulus palju aega dokumentatsiooni ja videote uurimiseks. Samuti oli mul palju probleeme doсkeriga, nimelt andmebaasi loomisega 
ja salvestamisega. Arvasin ekslikult, et õppevideosid jälgides suudan dockeri ja andmebaasi paari päevaga üles seada. Muude võimaluste 
puudumisel pöördusin Spring Bootiga tehisintellekti poole. See aitas mul valida õiged sõltuvused ja järgida häid tavasid, kuid kui selle 
loogikas tekkisid vead, ei suutnud ma neid ilma kogemusteta märgata. Väiksem probleem oli ka see, et ma ei olnud kunagi lennukis istekohtade 
reserveerimist kasutanud, nii et ma ei teadnud, kuidas see toimib. Minu päästjaks oli kogenum inimene, kes suutis mulle lihtsas keeles 
selgitada, kuidas Spring Bootiga töötada. Palju katsetamise ja eksimuste abil sain backend'i valmis, kuid frontend'i jaoks ei jäänud peaaegu 
üldse aega. Kui ma oleksin saanud projektile rohkem aega kulutada ja oma aega paremini hallata, oleksin tähtajast kinni pidanud, kuid vaatamata 
hilinemisele soovin ma selle projekti siiski lõpule viia.

## Kasutatud materjalid <a name="materials"></a>

[How To Create Multi Containers Using Docker Compose](https://youtu.be/yodeo205pp0?si=ux-mjN7XJWxAHUFx)
- compose.yaml ja Dockerfile on kirjutatud selle video abil

[Airline seat maps](https://www.altexsoft.com/blog/airline-seat-maps/)
- Inspiratsiooniallikas

Erinevad juhendid jakarta, lombok, spring boot ja vue jaoks
- https://www.javaguides.net/2023/07/jpa-column-annotation.html
- https://auth0.com/blog/a-complete-guide-to-lombok/
- https://cli.vuejs.org/config/
- https://github.com/arsy786/springboot-best-practices

Eelmised vue projektid meeldetuletamiseks

### ChatGPT
- Aitas teisendada minu andmebaasi kirjelduse koodiks (pidin ise tegema palju muudatusi ja ümberkujundusi protsessi käigus)
- Selgitas vigu ja pakkus erinevaid lahendusi
- Aitas mitte teha vigu FlightService'i meetodite kirjutamisel ja refaktoriseerimisel
- Kirjutas DTO, et vabaneda rekursioonidest
- Kirjutas WebConfig
- Koostas enamiku vue komponentidest ja aitas lahendada probleeme (palju asju pidin siiski ise parandama)
- Genereeris juhuslikud väärtused andmebaasi täitmiseks
- Kirjutas SeatRecommenderService, kasutades minu poolt kirjeldatud algoritmi
