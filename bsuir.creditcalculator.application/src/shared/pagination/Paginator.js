class Paginator {
    constructor() {
        this.currentPage = 0;
        this.minimalPage = 0;
        this.maximalPage = 0;
        this.itemsPerPage = 0;
    }

    pageLeft = () => {
        if (this.currentPage > this.minimalPage) {
            this.currentPage -= 1;
        }
    };

    pageRight = () => {
        if (this.currentPage < this.maximalPage) {
            this.currentPage += 1;
        }
    };

    pageStart = () => {
        this.currentPage = this.minimalPage;
    };

    pageEnd = () => {
        this.currentPage = this.maximalPage;
    };

    updateMaximalPage = itemsCount => {
        this.maximalPage = Math.ceil(itemsCount / this.itemsPerPage) - 1;
    };
}

export default Paginator;
