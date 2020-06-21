import 'vue-smooth-dnd';
import { DropResult } from 'vue-smooth-dnd';
export const applyDrag = (arr: any[], dropResult: DropResult) => {
    const { removedIndex, addedIndex, payload } = dropResult;
    const result = [...arr];
    let itemToAdd = payload;
    if (removedIndex != null) {
        itemToAdd = result.splice(removedIndex, 1)[0];
    }
    if (addedIndex != null) {
        result.splice(addedIndex, 0, itemToAdd);
    }
    return result;
};

export const getMaxInd = (arr: Array<{ index: number }>) => {
    // tslint:disable-next-line: variable-name
    let mx_index = 0;
    for (let i = 0; i < arr.length; i++) {
        if (mx_index < arr[i].index) {
            mx_index = arr[i].index;
        }
    }
    return mx_index + 1;
};