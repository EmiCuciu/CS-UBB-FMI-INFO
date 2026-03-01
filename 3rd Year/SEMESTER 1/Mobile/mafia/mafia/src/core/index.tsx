export const getLogger: (tag: string) => (...args: unknown[]) => void =
    tag => (...args) => console.log(tag, ...args);

export { useNetwork } from './useNetwork';
export { storageService } from './storage';
export type { PendingOperation } from './storage';
