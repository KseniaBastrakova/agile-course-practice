package ru.unn.agile.RatioCalculator.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.unn.agile.RatioCalculator.Model.Ratio;
import ru.unn.agile.RatioCalculator.viewmodel.ViewModel;
import ru.unn.agile.RatioCalculator.infrastructure_lab3.TxtLogger;

public class Calculator {
    @FXML
    void initialize() {
        viewModel.setLogger(new TxtLogger("./TxtLogger-lab3.log"));
        final ChangeListener<Boolean> focusChangeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable,
                                final Boolean oldValue, final Boolean newValue) {
                viewModel.onFocusChanged(oldValue, newValue);
            }
        };

    numeratorFirst.textProperty().bindBidirectional(viewModel.numeratorFirstProperty());
    numeratorFirst.focusedProperty().addListener(focusChangeListener);
    numeratorSecond.textProperty().bindBidirectional(viewModel.numeratorSecondProperty());
    numeratorSecond.focusedProperty().addListener(focusChangeListener);
    denominatorFirst.textProperty().bindBidirectional(viewModel.denominatorFirstProperty());
    denominatorFirst.focusedProperty().addListener(focusChangeListener);
    denominatorSecond.textProperty().bindBidirectional(viewModel.denominatorSecondProperty());
    denominatorSecond.focusedProperty().addListener(focusChangeListener);

    cbOperation.valueProperty().bindBidirectional(viewModel.operationProperty());


    cbOperation.valueProperty().addListener(new ChangeListener<Ratio.Operation>() {
            @Override
            public void changed(final ObservableValue<? extends Ratio.Operation> observable,
                                final Ratio.Operation oldValue,
                                final Ratio.Operation newValue) {
                viewModel.onOperationChanged(oldValue, newValue);
            }
        });
    calc.setOnAction(event -> viewModel.calculate());
    }

    @FXML
    private ViewModel viewModel;
    @FXML
    private TextField numeratorFirst;
    @FXML
    private TextField numeratorSecond;
    @FXML
    private TextField denominatorFirst;
    @FXML
    private TextField denominatorSecond;
    @FXML
    private ComboBox<Ratio.Operation> cbOperation;
    @FXML
    private Button calc;

}
