package ru.unn.agile.RatioCalculator.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.unn.agile.RatioCalculator.Model.Ratio;
import ru.unn.agile.RatioCalculator.viewmodel.ViewModel;
import ru.unn.agile.RatioCalculator.Infrastructure.TxtLogger;

public class Calculator {
    @FXML
    void initialize() {
        viewModel.setLogger(new TxtLogger("./txt.log"));
        final ChangeListener<Boolean> changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observableValue,
                                final Boolean old, final Boolean newValue) {
                viewModel.onFocusChanged(old, newValue);
            }
        };

    numeratorFirst.textProperty().bindBidirectional(viewModel.numeratorFirstProperty());
    numeratorFirst.focusedProperty().addListener(changeListener);
    numeratorSecond.textProperty().bindBidirectional(viewModel.numeratorSecondProperty());
    numeratorSecond.focusedProperty().addListener(changeListener);
    denominatorFirst.textProperty().bindBidirectional(viewModel.denominatorFirstProperty());
    denominatorFirst.focusedProperty().addListener(changeListener);
    denominatorSecond.textProperty().bindBidirectional(viewModel.denominatorSecondProperty());
    denominatorSecond.focusedProperty().addListener(changeListener);

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
