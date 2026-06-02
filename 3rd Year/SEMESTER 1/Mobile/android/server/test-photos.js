// Test manual pentru salvare poze
// Rulează cu: node test-photos.js

const fs = require('fs');
const path = require('path');

// Simulează o poză mică Base64 (1x1 pixel PNG)
const testPhoto = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";

const photosPath = path.join(__dirname, 'db', 'photos.json');

console.log('🧪 Testing photo save...');
console.log('Photo size:', testPhoto.length, 'chars');

// Read current photos
let photosData = { photos: [] };
if (fs.existsSync(photosPath)) {
    photosData = JSON.parse(fs.readFileSync(photosPath, 'utf8'));
}

console.log('Current photos count:', photosData.photos.length);

// Add test photo
const testCharacterId = "test_character_id_123";
photosData.photos.push({
    id: "test_photo_" + Date.now(),
    characterId: testCharacterId,
    data: testPhoto,
    createdAt: new Date().toISOString()
});

// Save
fs.writeFileSync(photosPath, JSON.stringify(photosData, null, 2));

console.log('✅ Photo saved!');
console.log('New photos count:', photosData.photos.length);

// Verify
const savedData = JSON.parse(fs.readFileSync(photosPath, 'utf8'));
console.log('✅ Verification: photos.json contains', savedData.photos.length, 'photo(s)');

if (savedData.photos.length > 0) {
    const lastPhoto = savedData.photos[savedData.photos.length - 1];
    console.log('Last photo:', {
        id: lastPhoto.id,
        characterId: lastPhoto.characterId,
        dataLength: lastPhoto.data.length,
        createdAt: lastPhoto.createdAt
    });
}

