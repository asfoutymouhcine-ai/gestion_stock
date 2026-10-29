# StockPulse — "Users & Security" module (Youssef Lagmouch)

Advanced Java project — EMSI 4IIR G9. JavaFX application, in-memory data.

## Open in Eclipse
1. Unzip `stockpulse.zip`.
2. In Eclipse: **File > Import > Maven > Existing Maven Projects** > select the `stockpulse` folder > Finish.
3. Wait for the dependency download to finish (Maven downloads JavaFX and JUnit).
4. Right-click `src/main/java/ma/emsi/stockpulse/Main.java` > **Run As > Java Application**.

If the launch fails: right-click the project > **Run As > Maven build...** > Goals: `javafx:run`.

## Test accounts
| Username | Password | Role |
|---|---|---|
| admin | admin123 | Administrator |
| magasinier | mag123 | Storekeeper |

## User interface
- The **login page** (UC1) has no sidebar: just the StockPulse logo and the sign-in form.
- Once the user is logged in, a **left sidebar** appears with the logo and a single **User** dropdown. Click it to show:
  *Manage users* (Administrator only, UC2), *Change password* (UC3) and *Log out*.
- Click the logo to go back to the Home page.
- The other members add their own dropdown (Catalog, Stock, Inventory) below the *User* one.

## Tests
Right-click the project > **Run As > JUnit Test** (or `mvn test`).

## Structure
- `model`: Role, Utilisateur (abstract), Administrateur, Magasinier
- `repository`: IRepository<T>, InMemoryRepository<T> (temporary)
- `service`: AuthService (UC1), UtilisateurService (UC2, UC3)
- `exception`: AuthentificationException
- `ui`: MenuPrincipal (4 JavaFX screens + sidebar), `stockpulse.css` (styles), `logo.png` and `logo-icon.png` (StockPulse logo)
- `Main`: common entry point
