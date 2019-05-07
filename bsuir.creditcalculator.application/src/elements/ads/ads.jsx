import React from "react";
import PropTypes from "prop-types";

import uberbankAd from "../../resources/uberbank.svg";
import superduperbankAd from "../../resources/superduperbank.svg";

import styles from "./ads.scss";

const Ads = props => {
  const { children } = props;

  return (
    <div className={styles.ads}>
      <img src={uberbankAd} alt="Looser!" />
      <div className={styles["ads-container"]}>{children}</div>
      <img src={superduperbankAd} alt="Looser!" />
    </div>
  );
};

Ads.propTypes = {
  children: PropTypes.shape().isRequired
};

export const withAds = Component => props => (
  <Ads>
    <Component {...props} />
  </Ads>
);
