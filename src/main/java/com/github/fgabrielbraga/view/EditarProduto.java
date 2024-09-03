package com.github.fgabrielbraga.view;

import com.github.fgabrielbraga.controller.EditarProdutoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.github.fgabrielbraga.model.Product;

import java.io.IOException;

public class EditarProduto extends Application {

    private Product productEdit;

    public EditarProduto(Product productEdit) {
        this.productEdit = productEdit;
    }

    private final String title = "Editar Product";

    private static Stage window;

    private void setWindow(Stage window) {
        this.window = window;
    }

    public static Stage getWindow() {
        return window;
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/github/fgabrielbraga/view/fxml/editar_produto.fxml"));
            Parent root = fxmlLoader.load();

            EditarProdutoController editarProdutoController = (EditarProdutoController) fxmlLoader.getController();
            editarProdutoController.fillFields(productEdit);

            Scene scene = new Scene(root);

            setWindow(stage);
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(ConsultarProdutos.getWindow());
            stage.setTitle(title);
            stage.setResizable(false);
            stage.getIcons().add(new Icon().getImage());
            stage.show();
        } catch (IOException ex) {
            System.err.println(String.format("ERRO (%s)", title));
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
