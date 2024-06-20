const searchData = (searchValue, originalData) => {
    if (searchValue) {
        const data = originalData?.filter((obj) => {
            let flag = false;
            const regex = new RegExp(searchValue, 'gi');
            const dataArray = Object.values(obj);

            for (let i = 0; i < dataArray?.length; i++) {
                if (typeof dataArray[i] === 'string' && dataArray[i].match(regex)) {
                    flag = true;
                    break;
                }
            }

            return flag;
        });
        return data;
    }
    return originalData;
};

export default searchData;
