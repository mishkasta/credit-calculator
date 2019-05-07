package by.bsuir.Common.Interfaces;

public interface IEncoder {
    String encode(String inputString, String salt);

    boolean checkIfMatches(String rawString, String encodedString);
}
