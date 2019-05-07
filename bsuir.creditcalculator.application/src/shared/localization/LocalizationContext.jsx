import React from 'react';

import CommonConstants from '../../constants/CommonConstants';
import englishMessages from '../../constants/messages/messages.en.json';
import russianMessages from '../../constants/messages/messages.ru.json';
import belarussianMessages from '../../constants/messages/messages.be.json';

export const Locales = {
    ENGLISH: 'en',
    RUSSIAN: 'ru',
    BELARUSSIAN: 'be',
};

let mergedEnglishMessages = Object.assign({}, englishMessages);
const mergedBelarussianMessages = Object.assign(mergedEnglishMessages, belarussianMessages);
mergedEnglishMessages = Object.assign({}, englishMessages);
const mergedRussianMessages = Object.assign(mergedEnglishMessages, russianMessages);

export const getLocale = () => {
    const currentLocale = localStorage.getItem(CommonConstants.CURRENT_LOCALE_FLAG);

    switch (currentLocale) {
        case Locales.RUSSIAN:
            return mergedRussianMessages;
        case Locales.BELARUSSIAN:
            return mergedBelarussianMessages;
        case Locales.ENGLISH:
        default:
            return englishMessages;
    }
};

export const setLocale = locale => {
    localStorage.setItem(CommonConstants.CURRENT_LOCALE_FLAG, locale);
};

export const {
    Provider: LocalizationProvider,
    Consumer: LocalizationConsumer,
} = React.createContext({
    messages: getLocale(),
    onLanguageSelect: () => {
    },
});

export const localized = Component => props => (
    <LocalizationConsumer>
        {({ messages, onLanguageSelect }) => (
            <Component {...props} messages={messages} onLanguageSelect={onLanguageSelect} />
        )}
    </LocalizationConsumer>
);
