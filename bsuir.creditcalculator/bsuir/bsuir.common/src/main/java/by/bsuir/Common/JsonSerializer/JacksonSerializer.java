package by.bsuir.Common.JsonSerializer;

import by.bsuir.Common.Interfaces.IJsonSerializer;
import by.bsuir.Common.OperationResult.OperationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JacksonSerializer implements IJsonSerializer {
    private final ObjectMapper _objectMapper;


    public JacksonSerializer() {
        _objectMapper = new ObjectMapper();
    }

    @Override
    public OperationResult<SerializeError, String> serialize(Object object) {
        try {
            String serializedObject = _objectMapper.writeValueAsString(object);

            return OperationResult.createSuccessful(serializedObject);
        } catch (IOException e) {
            return OperationResult.createUnsuccessful();
        }
    }

    @Override
    public <TType> OperationResult<SerializeError, TType> deserialize(String serializedObject, Class<TType> type) {
        try {
            TType deserializedObject = _objectMapper.readValue(serializedObject, type);

            return OperationResult.createSuccessful(deserializedObject);
        } catch (IOException e) {
            return OperationResult.createUnsuccessful();
        }
    }

    @Override
    public <TType> OperationResult<SerializeError, TType> deserialize(InputStream inputStream, Class<TType> type) {
        try {
            TType deserializedObject = _objectMapper.readValue(inputStream, type);

            return OperationResult.createSuccessful(deserializedObject);
        } catch (IOException e) {
            return OperationResult.createUnsuccessful();
        }
    }
}
