package by.bsuir.Common.Interfaces;

public interface IErrorMessageProvider {
    <TErrorType extends Enum>  String provide(TErrorType error, Class<TErrorType> errorType);
}
