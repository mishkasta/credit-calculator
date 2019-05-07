package by.bsuir.Common.Interfaces;

import by.bsuir.Common.NumberConverter.ConvertNumberError;
import by.bsuir.Common.OperationResult.OperationResult;

public interface INumberConverter {
    OperationResult<ConvertNumberError, Integer> convertToInteger(String input);

    OperationResult<ConvertNumberError, Double> convertToDouble(String input);
}
