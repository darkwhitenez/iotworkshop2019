# Symdroid

Ovo je repozitorij za Android aplikaciju razvijenu u sklopu IoT Workshop hackatona 2019.

## Aplikacija ima sljedeće funkcije
	* jednostavnu pretraga javnih senzorskih resursa kroz platformu symbiote-h2020
	* prikaz uređaja na google maps karti
	* spremanje metainformacija o senzorskim resursima u memoriju uređaja (u listu pretplaćenih resursa)
	* dohvat podataka željenih resursa (ako su dostupni)


## Aplikacija nudi mogućnost konfiguracije sljedećih parametara
	* URL Symbiote Core Servisa
	* maksimalni broj stavki na karti prilikom pretrage resursa (zbog memorijskog ograničenja Androida)
	* maksimalni broj očitanja senzora prilikom dohvata podataka (zbog memorijskog ograničenja Androida)
	* maksimalni timeout čitanja podataka s API-ja

## Upute za pokretanje
	* potrebno je registrirati novu android aplikaciju u Google Developer Console https://console.developers.google.com/
	* omogućiti korištenje Maps SDK
	* generirati API KEY i postaviti ga u AndroidManifest.xml
	


## Screenshots

Home screen
<p align="center">
<img width="250" src="/screenshots/1.jpg">
</p> 

---

Subscribed resources
<p align="center">
<img width="250" src="/screenshots/2.jpg">
</p>

---

Map
<p align="center">
<img width="250" src="/screenshots/3.jpg">
</p>

---

Sensor data
<p align="center">
<img width="250" src="/screenshots/4.jpg">
</p>

---

Queried resources
<p align="center">
<img width="250" src="/screenshots/5.jpg">
</p>

---