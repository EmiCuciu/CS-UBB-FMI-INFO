// Image compression utility to reduce photo size before uploading
export const compressImage = async (base64String: string, maxWidth: number = 800, quality: number = 0.7): Promise<string> => {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;

            // Calculate new dimensions while maintaining aspect ratio
            if (width > maxWidth) {
                height = (height * maxWidth) / width;
                width = maxWidth;
            }

            canvas.width = width;
            canvas.height = height;

            const ctx = canvas.getContext('2d');
            if (!ctx) {
                reject(new Error('Failed to get canvas context'));
                return;
            }

            ctx.drawImage(img, 0, 0, width, height);

            // Convert to base64 with compression
            const compressedBase64 = canvas.toDataURL('image/jpeg', quality);
            // Remove data URL prefix
            const base64Data = compressedBase64.replace(/^data:image\/\w+;base64,/, '');
            resolve(base64Data);
        };

        img.onerror = () => {
            reject(new Error('Failed to load image'));
        };

        img.src = `data:image/jpeg;base64,${base64String}`;
    });
};

