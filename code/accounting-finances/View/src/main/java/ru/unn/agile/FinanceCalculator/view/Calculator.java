package ru.unn.agile.FinanceCalculator.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ru.unn.agile.FinanceCalculator.Model.ExpensesType;
import ru.unn.agile.FinanceCalculator.viewmodel.ViewModel;

public class Calculator {
    @FXML
    private ViewModel viewModel;
    @FXML
    private DatePicker inputData;
    @FXML
    private DatePicker outputData;
    @FXML
    private ComboBox<ExpensesType> expencesBox;
    @FXML
    private TextField inputCost;
    @FXML
    private Button input;
    @FXML
    private Button output;

    @FXML
    void initialize() {

        // Two-way binding hasn't supported by FXML yet, so place it in code-behind
        inputData.valueProperty().bindBidirectional(viewModel.dateInputProperty());
        outputData.valueProperty().bindBidirectional(viewModel.dateOutputProperty());
        inputCost.textProperty().bindBidirectional(viewModel.inputExpensesCostProperty());

        expencesBox.valueProperty().bindBidirectional(viewModel.expensesTypesProperty());

        output.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                viewModel.getCosts();
            }
        });

        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                viewModel.submitCosts();
            }
        });
    }
}
