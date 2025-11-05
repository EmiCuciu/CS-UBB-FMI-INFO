import { MafiotProps } from '../todo/MafiotProps';

const getLogger: (tag: string) => (...args: unknown[]) => void =
    tag => (...args) => console.log(tag, ...args);

const log = getLogger('Storage');

const MAFIOTS_KEY = 'mafiots';
const PENDING_OPS_KEY = 'pendingOperations';

export interface PendingOperation {
    id: string;
    type: 'create' | 'update' | 'delete';
    mafiot: MafiotProps;
    timestamp: number;
}

export const storageService = {
    // Mafiots storage
    saveMafiots: (mafiots: MafiotProps[]) => {
        const stored = storageService.getMafiots();
        const updatedMap = new Map(stored.map(m => [m.id, m]));

        mafiots.forEach(mafiot => {
            updatedMap.set(mafiot.id, mafiot);
        });

        const updatedList = Array.from(updatedMap.values());
        localStorage.setItem(MAFIOTS_KEY, JSON.stringify(updatedList));
    },

    getMafiots: (): MafiotProps[] => {
        try {
            const data = localStorage.getItem(MAFIOTS_KEY);
            return data ? JSON.parse(data) : [];
        } catch (error) {
            log('Error loading mafiots:', error);
            return [];
        }
    },

    // Pending operations storage
    addPendingOperation: (operation: Omit<PendingOperation, 'id' | 'timestamp'>) => {
        try {
            const operations = storageService.getPendingOperations();
            const newOp: PendingOperation = {
                ...operation,
                id: Date.now().toString(),
                timestamp: Date.now(),
            };
            operations.push(newOp);
            localStorage.setItem(PENDING_OPS_KEY, JSON.stringify(operations));
            log('Added pending operation:', newOp);
            return newOp;
        } catch (error) {
            log('Error adding pending operation:', error);
            throw error;
        }
    },

    getPendingOperations: (): PendingOperation[] => {
        try {
            const data = localStorage.getItem(PENDING_OPS_KEY);
            return data ? JSON.parse(data) : [];
        } catch (error) {
            log('Error loading pending operations:', error);
            return [];
        }
    },

    removePendingOperation: (id: string) => {
        try {
            const operations = storageService.getPendingOperations();
            const filtered = operations.filter(op => op.id !== id);
            localStorage.setItem(PENDING_OPS_KEY, JSON.stringify(filtered));
            log('Removed pending operation:', id);
        } catch (error) {
            log('Error removing pending operation:', error);
        }
    },

    clearPendingOperations: () => {
        try {
            localStorage.removeItem(PENDING_OPS_KEY);
            log('Cleared all pending operations');
        } catch (error) {
            log('Error clearing pending operations:', error);
        }
    },
};
