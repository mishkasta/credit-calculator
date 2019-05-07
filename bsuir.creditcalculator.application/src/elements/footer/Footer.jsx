import React from 'react';

import styles from './Footer.scss';

const Footer = () => (
    <footer className={styles.footer}>
        <div className="footer-copyright text-center">
            {`© ${new Date().getFullYear()} BSUIR Knowledge`}
        </div>
    </footer>
);

export default Footer;
