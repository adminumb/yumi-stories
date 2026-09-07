// Итерация 5: страница просмотра одной истории целиком.
// id истории передаётся через query-параметр: story.html?id=3

const contentDiv = document.getElementById('story-content');

function getStoryIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

async function loadStoryDetail() {
    const storyId = getStoryIdFromUrl();

    if (!storyId) {
        contentDiv.textContent = '❌ Не указан id истории.';
        return;
    }

    try {
        const [storyResponse, scenesResponse] = await Promise.all([
            fetch(`/api/stories/${storyId}`),
            fetch(`/api/stories/${storyId}/scenes`)
        ]);

        if (!storyResponse.ok) {
            throw new Error(storyResponse.status === 404
                ? 'История не найдена'
                : `Сервер ответил с ошибкой: ${storyResponse.status}`);
        }

        const story = await storyResponse.json();
        const scenes = scenesResponse.ok ? await scenesResponse.json() : [];

        renderStory(story, scenes);
    } catch (error) {
        contentDiv.textContent = `❌ ${error.message}`;
    }
}

function renderStory(story, scenes) {
    contentDiv.innerHTML = '';

    const title = document.createElement('h1');
    title.textContent = story.title;
    contentDiv.appendChild(title);

    const idea = document.createElement('p');
    idea.className = 'detail-idea';
    idea.textContent = story.idea;
    contentDiv.appendChild(idea);

    // Галерея фото
    if (story.photoFilenames && story.photoFilenames.length > 0) {
        const gallery = document.createElement('div');
        gallery.className = 'detail-gallery';

        story.photoFilenames.forEach(filename => {
            const img = document.createElement('img');
            img.src = `/photos/${filename}`;
            img.alt = story.title;
            img.className = 'detail-photo';
            gallery.appendChild(img);
        });

        contentDiv.appendChild(gallery);
    }

    // Сцены как последовательность
    const scenesHeading = document.createElement('h2');
    scenesHeading.textContent = 'Сцены';
    contentDiv.appendChild(scenesHeading);

    if (scenes.length === 0) {
        const emptyMsg = document.createElement('p');
        emptyMsg.className = 'detail-empty';
        emptyMsg.textContent = 'У этой истории пока нет сцен.';
        contentDiv.appendChild(emptyMsg);
        return;
    }

    const scenesTimeline = document.createElement('div');
    scenesTimeline.className = 'detail-timeline';

    scenes.forEach(scene => {
        const sceneItem = document.createElement('div');
        sceneItem.className = 'detail-scene';

        const sceneNumber = document.createElement('span');
        sceneNumber.className = 'detail-scene-number';
        sceneNumber.textContent = scene.orderIndex + 1;

        const sceneText = document.createElement('p');
        sceneText.textContent = scene.text;

        sceneItem.appendChild(sceneNumber);
        sceneItem.appendChild(sceneText);
        scenesTimeline.appendChild(sceneItem);
    });

    contentDiv.appendChild(scenesTimeline);
}

loadStoryDetail();