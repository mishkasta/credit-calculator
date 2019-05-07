package by.bsuir.Common.Interfaces;

import by.bsuir.Common.JsonSerializer.SerializeError;
import by.bsuir.Common.OperationResult.OperationResult;

import java.io.InputStream;

public interface IJsonSerializer {
    OperationResult<SerializeError, String> serialize(Object object);

    <TType> OperationResult<SerializeError, TType> deserialize(String serializedObject, Class<TType> type);

    <TType> OperationResult<SerializeError, TType> deserialize(InputStream inputStream, Class<TType> type);
}
