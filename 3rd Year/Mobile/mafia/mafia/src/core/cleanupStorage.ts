// Utility to clean up localStorage and remove photo data
// Run this in the browser console if you're experiencing QuotaExceededError

/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */

export const cleanupStorage = () => {
    try {
        // Get current mafiots
        const mafiotsData = localStorage.getItem('mafiots');
        if (mafiotsData) {
            const mafiots = JSON.parse(mafiotsData);

            // Remove photo base64 data from all mafiots
            const cleanedMafiots = mafiots.map((m: any) => {
                const { photo, ...rest } = m;
                return rest;
            });

            localStorage.setItem('mafiots', JSON.stringify(cleanedMafiots));
            console.log(`Cleaned ${mafiots.length} mafiots, removed photo data`);
        }

        // Clear pending operations if needed
        const pendingOps = localStorage.getItem('pendingOperations');
        if (pendingOps) {
            const ops = JSON.parse(pendingOps);
            const cleanedOps = ops.map((op: any) => {
                if (op.mafiot?.photo) {
                    const { photo, ...rest } = op.mafiot;
                    return { ...op, mafiot: rest };
                }
                return op;
            });
            localStorage.setItem('pendingOperations', JSON.stringify(cleanedOps));
            console.log(`Cleaned ${ops.length} pending operations`);
        }

        console.log('✓ Storage cleanup complete!');
        console.log('You can now reload the page.');

    } catch (error) {
        console.error('Error during cleanup:', error);
    }
};

// Auto-run cleanup on import
if (typeof window !== 'undefined') {
    console.log('To clean up storage, run: cleanupStorage()');
    (window as any).cleanupStorage = cleanupStorage;
}


