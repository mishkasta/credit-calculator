import numeral from 'numeral';

import CommonConstants from '../constants/CommonConstants';

class NumberFormatter {
    static format = number => {
        return numeral(number).format(CommonConstants.NUMBER_FORMAT);
    };
}

export default NumberFormatter;
