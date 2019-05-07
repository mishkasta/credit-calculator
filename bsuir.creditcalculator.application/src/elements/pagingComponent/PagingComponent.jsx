import React from 'react';
import PropTypes from 'prop-types';
import { Button, ButtonGroup } from 'react-bootstrap';

import { FastBackwardGlyph, FastForwardGlyph, StepBackwardGlyph, StepForwardGlyph } from '../glyphs/Glyphs'

import styles from './PagingComponent.scss';

const PagingComponent = props => {
    const {
        pageLeft,
        pageStart,
        pageRight,
        pageEnd,
        minimalPage,
        maximalPage,
        currentPage } = props;

    const canPage = minimalPage !== maximalPage;
    const canPageLeft = canPage && currentPage > minimalPage;
    const canPageRight = canPage && currentPage < maximalPage;

    return (
        <ButtonGroup className={styles['paging-component']} aria-label="Basic example">
            <Button variant="light" disabled={!canPageLeft} onClick={() => pageStart()}>
                <FastBackwardGlyph />
            </Button>
            <Button variant="light" disabled={!canPageLeft} onClick={() => pageLeft()}>
                <StepBackwardGlyph />
            </Button>
            <Button variant="light" disabled={!canPageRight} onClick={() => pageRight()}>
                <StepForwardGlyph />
            </Button>
            <Button variant="light" disabled={!canPageRight} onClick={() => pageEnd()}>
                <FastForwardGlyph />
            </Button>
        </ButtonGroup>
    );
};

PagingComponent.propTypes = {
    pageLeft: PropTypes.func.isRequired,
    pageStart: PropTypes.func.isRequired,
    pageRight: PropTypes.func.isRequired,
    pageEnd: PropTypes.func.isRequired,
    minimalPage: PropTypes.number.isRequired,
    maximalPage: PropTypes.number.isRequired,
    currentPage: PropTypes.number.isRequired
};

export default PagingComponent;
