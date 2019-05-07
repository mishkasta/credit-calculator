package by.bsuir.Common.NumberConverter;

import by.bsuir.Common.Interfaces.INumberConverter;
import by.bsuir.Common.OperationResult.OperationResult;

public class NumberConverter implements INumberConverter {
    @Override
    public OperationResult<ConvertNumberError, Integer> convertToInteger(String input) {
        try {
            int value = Integer.parseInt(input);

            return OperationResult.createSuccessful(value);
        } catch (NumberFormatException ex) {
            return OperationResult.createUnsuccessful();
        }
    }

    @Override
    public OperationResult<ConvertNumberError, Double> convertToDouble(String input) {
        try {
            double value = Double.parseDouble(input);

            return OperationResult.createSuccessful(value);
        } catch (NumberFormatException ex) {
            return OperationResult.createUnsuccessful();
        }
    }
}
