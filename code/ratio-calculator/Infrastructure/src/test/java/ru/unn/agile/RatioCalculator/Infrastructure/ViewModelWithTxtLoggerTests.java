package ru.unn.agile.RatioCalculator.infrastructure_lab3;

import ru.unn.agile.RatioCalculator.viewmodel.*;

public class ViewModelWithTxtLoggerTests extends ViewModelTests {
    @Override
    public void setUp() {
        TxtLogger realLogger = new TxtLogger("./ViewModel_with_TxtLogger_Tests-lab3.log");
        super.setExternalViewModel(new ViewModel(realLogger));
    }
}
