package ma.emsi.stockpulse.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.emsi.stockpulse.exception.AuthentificationException;
import ma.emsi.stockpulse.model.Role;
import ma.emsi.stockpulse.model.Utilisateur;
import ma.emsi.stockpulse.service.AuthService;
import ma.emsi.stockpulse.service.UtilisateurService;

/**
 * Login screen and menus. The login page has no sidebar; once the user is
 * logged in, a left sidebar appears with one "User" dropdown.
 * Screens: Login (UC1), Home, Manage users (UC2), Change password (UC3).
 */
public class MenuPrincipal extends Application {
    private static AuthService authService;
    private static UtilisateurService utilisateurService;

    private Stage stage;
    private BorderPane mainLayout;   // sidebar on the left, current page in the center
    private Button usersItem;        // only created for the Administrator
    private Button passwordItem;

    public static void configurer(AuthService a, UtilisateurService u) {
        authService = a;
        utilisateurService = u;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("StockPulse");
        stage.getIcons().add(new Image(MenuPrincipal.class.getResource("logo-icon.png").toExternalForm()));
        stage.setMinWidth(860);
        stage.setMinHeight(560);
        showLogin();
        stage.show();
    }

    private void showScene(Parent root) {
        if (stage.getScene() == null) {
            Scene scene = new Scene(root, 1000, 640);
            scene.getStylesheets().add(MenuPrincipal.class.getResource("stockpulse.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
    }

    private ImageView logo(double width) {
        ImageView view = new ImageView(new Image(MenuPrincipal.class.getResource("logo.png").toExternalForm()));
        view.setFitWidth(width);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        return view;
    }

    // ---------- Login (UC1), no sidebar ----------
    private void showLogin() {
        Label title = new Label("Sign in to your account");
        title.getStyleClass().add("card-title");

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Label message = new Label();
        message.getStyleClass().add("error-text");

        Button signIn = new Button("Sign in");
        signIn.getStyleClass().add("primary-button");
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setDefaultButton(true);
        signIn.setOnAction(e -> {
            try {
                authService.login(username.getText(), password.getText());
                showMain();
            } catch (AuthentificationException ex) {
                message.setText(ex.getMessage());
                password.clear();
            }
        });

        VBox card = new VBox(12, logo(230), title, username, password, signIn, message);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28));
        card.setMaxWidth(360);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane page = new StackPane(card);
        page.getStyleClass().add("login-page");
        showScene(page);
    }

    // ---------- Layout shown after login: sidebar + pages ----------
    private void showMain() {
        mainLayout = new BorderPane();
        mainLayout.setLeft(buildSidebar());
        showScene(mainLayout);
        showHome();
    }

    private VBox buildSidebar() {
        Utilisateur user = authService.getUtilisateurCourant();

        ImageView logo = logo(120);
        logo.setCursor(Cursor.HAND);
        logo.setOnMouseClicked(e -> showHome());

        passwordItem = menuItem("Change password", this::showChangePassword);
        VBox userGroup;
        if (user.peutGererLeSysteme()) {
            usersItem = menuItem("Manage users", this::showUsers);
            userGroup = dropdown("User", usersItem, passwordItem);
        } else {
            usersItem = null;
            userGroup = dropdown("User", passwordItem);
        }

        Button logoutButton = new Button("Log out");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> logout());
        // TODO S3: the other members add their own dropdown here (Catalog, Stock, Inventory).

        Label signedIn = new Label("Signed in as " + user.getIdentifiant() + " (" + user.getRole() + ")");
        signedIn.getStyleClass().add("signed-in");
        signedIn.setWrapText(true);

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        VBox sidebar = new VBox(18, logo, userGroup, space, signedIn, logoutButton);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setAlignment(Pos.TOP_CENTER);
        return sidebar;
    }

    // A header button that shows or hides the items below it.
    private VBox dropdown(String title, Button... items) {
        Button header = new Button(title + "   ▸");
        header.getStyleClass().add("group-header");
        header.setMaxWidth(Double.MAX_VALUE);

        VBox list = new VBox(2, items);
        list.setPadding(new Insets(4, 0, 0, 14));
        list.setVisible(false);
        list.setManaged(false);

        header.setOnAction(e -> {
            boolean open = !list.isVisible();
            list.setVisible(open);
            list.setManaged(open);
            header.setText(title + (open ? "   ▾" : "   ▸"));
        });
        return new VBox(header, list);
    }

    private Button menuItem(String text, Runnable action) {
        Button item = new Button(text);
        item.getStyleClass().add("menu-item");
        item.setMaxWidth(Double.MAX_VALUE);
        item.setOnAction(e -> action.run());
        return item;
    }

    private void highlight(Button selected) {
        passwordItem.getStyleClass().remove("active");
        if (usersItem != null) {
            usersItem.getStyleClass().remove("active");
        }
        if (selected != null) {
            selected.getStyleClass().add("active");
        }
    }

    private void logout() {
        authService.deconnecter();
        showLogin();
    }

    // ---------- Home ----------
    private void showHome() {
        highlight(null);
        Utilisateur user = authService.getUtilisateurCourant();

        Label title = new Label("Home");
        title.getStyleClass().add("page-title");
        Label welcome = new Label("Welcome back, " + user.getIdentifiant() + " (" + user.getRole() + ").");
        Label hint = new Label("Open the User menu on the left to manage your account.");
        hint.getStyleClass().add("muted");

        VBox content = new VBox(12, title, welcome, hint);
        content.getStyleClass().add("content");
        mainLayout.setCenter(content);
    }

    // ---------- Manage users (UC2) ----------
    private void showUsers() {
        if (!authService.getUtilisateurCourant().peutGererLeSysteme()) {
            alert(Alert.AlertType.ERROR, "Access denied: Administrator only.");
            return;
        }
        highlight(usersItem);

        Label title = new Label("Manage users");
        title.getStyleClass().add("page-title");

        TableView<Utilisateur> table = new TableView<>();
        TableColumn<Utilisateur, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdentifiant()));
        TableColumn<Utilisateur, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRole().getLibelle()));
        TableColumn<Utilisateur, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isBloque() ? "Blocked" : "Active"));
        table.getColumns().add(colUsername);
        table.getColumns().add(colRole);
        table.getColumns().add(colStatus);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label("No users to display."));
        loadUsers(table);

        Button unblock = new Button("Unblock");
        unblock.getStyleClass().add("secondary-button");
        unblock.setOnAction(e -> {
            Utilisateur selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert(Alert.AlertType.WARNING, "Select a user first.");
                return;
            }
            try {
                utilisateurService.debloquer(selected.getId());
                loadUsers(table);
            } catch (AuthentificationException | IllegalArgumentException ex) {
                alert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });

        Button delete = new Button("Delete");
        delete.getStyleClass().add("danger-button");
        delete.setOnAction(e -> {
            Utilisateur selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert(Alert.AlertType.WARNING, "Select a user first.");
                return;
            }
            try {
                utilisateurService.supprimer(selected.getId());
                loadUsers(table);
            } catch (AuthentificationException | IllegalArgumentException ex) {
                alert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });

        Label addTitle = new Label("Add a user");
        addTitle.getStyleClass().add("card-title");
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password (6 characters min.)");
        password.setPrefWidth(210);
        ComboBox<Role> role = new ComboBox<>(FXCollections.observableArrayList(Role.values()));
        role.setValue(Role.MAGASINIER);
        Button create = new Button("Create");
        create.getStyleClass().add("primary-button");
        create.setOnAction(e -> {
            try {
                utilisateurService.creer(username.getText(), password.getText(), role.getValue());
                username.clear();
                password.clear();
                loadUsers(table);
            } catch (AuthentificationException | IllegalArgumentException ex) {
                alert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
        HBox form = new HBox(10, username, password, role, create);
        form.setAlignment(Pos.CENTER_LEFT);
        VBox addCard = new VBox(10, addTitle, form);
        addCard.getStyleClass().add("card");

        VBox content = new VBox(12, title, table, new HBox(10, unblock, delete), addCard);
        content.getStyleClass().add("content");
        VBox.setVgrow(table, Priority.ALWAYS);
        mainLayout.setCenter(content);
    }

    private void loadUsers(TableView<Utilisateur> table) {
        try {
            table.setItems(FXCollections.observableArrayList(utilisateurService.lister()));
        } catch (AuthentificationException ex) {
            alert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    // ---------- Change password (UC3) ----------
    private void showChangePassword() {
        highlight(passwordItem);

        Label title = new Label("Change password");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Choose a new password of at least 6 characters.");
        subtitle.getStyleClass().add("muted");

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Old password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New password");
        PasswordField confirmation = new PasswordField();
        confirmation.setPromptText("Confirm new password");

        Button update = new Button("Update password");
        update.getStyleClass().add("primary-button");
        update.setOnAction(e -> {
            try {
                utilisateurService.changerMotDePasse(oldPassword.getText(), newPassword.getText(),
                        confirmation.getText());
                alert(Alert.AlertType.INFORMATION, "Password updated.");
                showHome();
            } catch (AuthentificationException | IllegalArgumentException ex) {
                alert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("secondary-button");
        cancel.setOnAction(e -> showHome());

        VBox card = new VBox(12, oldPassword, newPassword, confirmation, new HBox(10, update, cancel));
        card.getStyleClass().add("card");
        card.setMaxWidth(400);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        VBox content = new VBox(14, title, subtitle, card);
        content.getStyleClass().add("content");
        mainLayout.setCenter(content);
    }

    private void alert(Alert.AlertType type, String message) {
        Alert a = new Alert(type, message);
        a.setTitle("StockPulse");
        a.setHeaderText(null);
        a.showAndWait();
    }
}
