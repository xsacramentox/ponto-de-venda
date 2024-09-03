package com.github.fgabrielbraga.controller;

import com.github.fgabrielbraga.controller.util.AlertBox;
import com.github.fgabrielbraga.controller.util.Helper;
import com.github.fgabrielbraga.controller.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.github.fgabrielbraga.model.Product;
import com.github.fgabrielbraga.model.dao.ProductDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarProdutoController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField fieldCode;

    @FXML
    private TextField fieldDescription;

    @FXML
    private TextField fieldSaleValue;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    private Product productEdit;

    public void fillFields(Product product) {
        this.productEdit = product;
        fieldCode.setText(Long.toString(product.getCode()));
        fieldDescription.setText(product.getDescription());
        fieldSaleValue.setText(Double.toString(product.getPrice()).replace(".", ","));
        fieldCode.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnCancel.setOnMouseClicked(click -> {
            closeWindow();
        });

        btnSubmit.setOnMouseClicked(click -> {
            edit();
        });

        fieldCode.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                fieldDescription.requestFocus();
        });

        fieldDescription.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                fieldSaleValue.requestFocus();
        });

        fieldSaleValue.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                edit();
        });

        Helper.addTextLimiter(fieldCode, 4);
        Helper.addTextLimiter(fieldDescription, 100);
        Helper.addTextLimiter(fieldSaleValue, 8);
    }

    private void edit() {
        Product product = getModel();
        if(product != null) {
            ProductDAO dao = new ProductDAO();
            if (dao.update(product)) {
                AlertBox.editionCompleted();
                closeWindow();
            } else {
                AlertBox.editionError();
            }
        }
    }

    private Product getModel() {
        Product product = null;
        String description = fieldDescription.getText();
        String saleValue = fieldSaleValue.getText().replace(",", ".");

        if (Validator.validateFields(description, saleValue)) {
            if (Validator.validateDouble(saleValue)) {
                product = new Product(productEdit.getCode(), description, Double.parseDouble(saleValue));
            } else {
                AlertBox.onlyNumbers();
            }
        } else {
            AlertBox.fillAllFields();
        }
        return product;
    }

    private void closeWindow() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }
}