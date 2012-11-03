A small Plants vs Zombies clone I did in High School. I don't expect to work on this much, but I am working of fixing up little things here and there.



To add your own towers and enemies edit Tower.csv or Enemies.csv in ./media/

*	The first line must be the number of towers/enemies. The first tower must be the moogle tower and the last must be the remove tower.
*	Separate values with commas
*	You must adhere to the following format or things may break
*	Towers will be formated as: name, health, power (of each bullet), fireRate, speed (how fast the bullet travels), and cost
*	Enemies will be formated as: name, health, power (of each attack), attackRate, and speed (how fast it moves)
*	If you add towers you may add tower specific bullet images in ./media/images/bullet/name.png
*	Towers you add must have corresponding name.png and name-selected.png images in ./media/images/menu/ Use template.psd
*	You must add images for towers and enemies in ./media/images/towers/name.png or ./media/images/enemies/name.png