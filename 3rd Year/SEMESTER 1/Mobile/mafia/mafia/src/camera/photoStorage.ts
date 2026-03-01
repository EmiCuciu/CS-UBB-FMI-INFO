import { Filesystem, Directory } from '@capacitor/filesystem';
import { getLogger } from '../core';
import { getPhoto } from '../todo/MafiotApi';

const log = getLogger('PhotoStorage');

export const photoStorage = {
    // Save photo to filesystem and return the filename
    savePhoto: async (base64Data: string, mafiotId?: string): Promise<string> => {
        try {
            const fileName = `photo_${mafiotId || Date.now()}.jpeg`;

            await Filesystem.writeFile({
                path: fileName,
                data: base64Data,
                directory: Directory.Data
            });

            log('Photo saved to filesystem:', fileName);
            return fileName;
        } catch (error) {
            log('Error saving photo:', error);
            throw error;
        }
    },

    // Load photo from filesystem, fallback to server if not found
    loadPhoto: async (fileName: string, mafiotId?: string): Promise<string | null> => {
        try {
            // Try to load from filesystem first
            const result = await Filesystem.readFile({
                path: fileName,
                directory: Directory.Data
            });

            log('Photo loaded from filesystem:', fileName);
            return result.data as string;
        } catch (error) {
            log('Error loading photo from filesystem, trying server:', error);

            // If filesystem fails and we have mafiotId, try to load from server
            if (mafiotId) {
                try {
                    const photoData = await getPhoto(mafiotId);
                    if (photoData) {
                        log('Photo loaded from server:', mafiotId);
                        // Try to save to filesystem for future use
                        try {
                            await Filesystem.writeFile({
                                path: fileName,
                                data: photoData,
                                directory: Directory.Data
                            });
                            log('Photo cached to filesystem:', fileName);
                        } catch (saveError) {
                            log('Error caching photo to filesystem:', saveError);
                        }
                        return photoData;
                    }
                } catch (serverError) {
                    log('Error loading photo from server:', serverError);
                }
            }

            return null;
        }
    },

    // Delete photo from filesystem
    deletePhoto: async (fileName: string): Promise<void> => {
        try {
            await Filesystem.deleteFile({
                path: fileName,
                directory: Directory.Data
            });

            log('Photo deleted from filesystem:', fileName);
        } catch (error) {
            log('Error deleting photo:', error);
        }
    }
};



