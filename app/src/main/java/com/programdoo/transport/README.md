# kratak uvod
## activity i fragmenti
activity obuhvata vise fragmenata. predstavlja jednu celinu, kao sto su vezbaci, zaposleni,
i tako dalje. u fragmentima treba praviti ui i sve sto se u njemu desava. tako da
bi fragmenti u vezbacu bili lista vezbaca i edit fragmenti, merenja, lista njegovih
termina itd.<br>
prilikom pravljenja activity-ja i fragment-a, treba anotirati klasu sa `@AndroidEntryPoint`
kako bi Hilt umeo da generise odgovarajuc kod. <br>
prilikom pravljenja view modela, potrebno je anotirati klasu sa `@HiltViewModel` iz istog
razloga.
<br><br>
