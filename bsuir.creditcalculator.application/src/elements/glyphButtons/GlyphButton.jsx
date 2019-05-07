import React from 'react';
import PropTypes from 'prop-types';

import styles from './GlyphButton.scss';

const GlyphButton = ({ onClick, children }) => (
    <button
        className={styles['glyph-button']}
        type="button"
        onClick={onClick}
    >
        {children}
    </button>
);

GlyphButton.defaultProps = {
    onClick: () => {},
    children: {},
};

GlyphButton.propTypes = {
    onClick: PropTypes.func,
    children: PropTypes.node,
};

export default GlyphButton;
