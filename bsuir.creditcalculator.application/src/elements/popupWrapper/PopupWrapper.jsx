import React from 'react';
import PropTypes from 'prop-types';

import { withLoadingIndication } from '../../shared/loadingIndication/LoadingIndicationStore';

import styles from './PopupWrapper.scss';

class PopupWrapper extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            isVisible: props.isVisible,
        };
    }

    static getDerivedStateFromProps(newProps) {
        return { isVisible: newProps.isVisible }
    }

    render() {
        const { isVisible } = this.state;
        const { children, onClose, hideLoadingIndication } = this.props;

        if (!isVisible) return null;

        return (
            // eslint-disable-next-line jsx-a11y/click-events-have-key-events, jsx-a11y/no-static-element-interactions
            <div
                className={styles['popup-wrapper']}
                onClick={
                    (event) => {
                        if (event.target === event.currentTarget) {
                            hideLoadingIndication();
                            onClose();
                        }
                    }
                }
            >
                {children}
            </div>
        );
    }
}

PopupWrapper.defaultProps = {
    isVisible: false,
    onClose: () => {},
};

PopupWrapper.propTypes = {
    isVisible: PropTypes.bool,
    onClose: PropTypes.func,
    children: PropTypes.node.isRequired,
    hideLoadingIndication: PropTypes.func.isRequired
};

export default withLoadingIndication(PopupWrapper);
