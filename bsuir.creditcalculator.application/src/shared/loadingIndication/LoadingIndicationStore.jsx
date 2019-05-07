import React from 'react';
import PropTypes from 'prop-types';

const {
    Consumer: LoadingIndicationConsumer,
    Provider: LoadingIndicationProvider,
} = React.createContext({
    shouldShowLoadingIndication: false,
    showLoadingIndication: () => {
    },
    hideLoadingIndication: () => {
    },
});

export class LoadingIndicationStore extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            shouldShowLoadingIndication: false,
        };

        this.clientsCount = 0;
    }

    showLoadingIndication = () => {
        this.clientsCount += 1;
        this.setState({
            shouldShowLoadingIndication: true,
        });
    };

    hideLoadingIndication = () => {
        this.clientsCount -= 1;
        if (this.clientsCount !== 0) {
            return;
        }

        setTimeout(() => {
            this.setState({
                shouldShowLoadingIndication: false,
            });
        }, 350);
    };

    render() {
        const { shouldShowLoadingIndication } = this.state;
        const context = {
            shouldShowLoadingIndication,
            showLoadingIndication: this.showLoadingIndication,
            hideLoadingIndication: this.hideLoadingIndication,
        };
        const { children } = this.props;

        return (
            <LoadingIndicationProvider value={context}>
                {children}
            </LoadingIndicationProvider>
        );
    };
}

LoadingIndicationStore.propTypes = {
    children: PropTypes.shape().isRequired,
};

export const withLoadingIndication = Component => props => (
    <LoadingIndicationConsumer>
        {({ shouldShowLoadingIndication, showLoadingIndication, hideLoadingIndication }) => (
            <Component
                {...props}
                shouldShowLoadingIndication={shouldShowLoadingIndication}
                showLoadingIndication={showLoadingIndication}
                hideLoadingIndication={hideLoadingIndication}
            />
)}
    </LoadingIndicationConsumer>
);
