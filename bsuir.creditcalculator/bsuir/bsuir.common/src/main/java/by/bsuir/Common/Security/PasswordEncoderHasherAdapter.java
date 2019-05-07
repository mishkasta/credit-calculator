package by.bsuir.Common.Security;

import by.bsuir.Common.Interfaces.IEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderHasherAdapter implements IEncoder {
    private final PasswordEncoder _passwordEncoder;


    public PasswordEncoderHasherAdapter(PasswordEncoder passwordEncoder) {
        _passwordEncoder = passwordEncoder;
    }


    @Override
    public String encode(String inputString, String salt) {
        String encryptedString = _passwordEncoder.encode(inputString);

        return encryptedString;
    }

    @Override
    public boolean checkIfMatches(String rawString, String encodedString) {
        return _passwordEncoder.matches(rawString, encodedString);
    }
}
