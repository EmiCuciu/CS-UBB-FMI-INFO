export const getLogger: (tag: string) => (...args: unknown[]) => void =
    tag => (...args) => console.log(tag, ...args);
