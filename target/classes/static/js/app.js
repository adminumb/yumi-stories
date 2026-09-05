// Итерация 1: единственная задача этого файла —
// доказать, что frontend может получить JSON от backend и отрисовать его.

const statusBox = document.getElementById('status-box');

async function checkBackend() {
    try {
        const response = await fetch('/api/ping');

        if (!response.ok) {
            throw new Error(`Сервер ответил с ошибкой: ${response.status}`);
        }

        const data = await response.json();

        statusBox.textContent = `✅ ${data.message} (status: ${data.status})`;
        statusBox.classList.add('ok');
    } catch (error) {
        statusBox.textContent = `❌ Не удалось связаться с сервером: ${error.message}`;
        statusBox.classList.add('error');
    }
}

checkBackend();

// --- Итерация 2: создание и отображение историй ---

const storyForm = document.getElementById('story-form');
const storiesList = document.getElementById('stories-list');

async function loadStories() {
    try {
        const response = await fetch('/api/stories');
        if (!response.ok) {
            throw new Error(`Сервер ответил с ошибкой: ${response.status}`);
        }
        const stories = await response.json();
        renderStories(stories);
    } catch (error) {
        storiesList.textContent = `❌ Не удалось загрузить истории: ${error.message}`;
    }
}

function renderStories(stories) {
    storiesList.innerHTML = '';

    if (stories.length === 0) {
        storiesList.textContent = 'Пока нет ни одной истории. Создай первую!';
        return;
    }

    stories.forEach(story => {
        const card = document.createElement('div');
        card.className = 'story-card';

        const title = document.createElement('h3');
        title.textContent = story.title;

        const idea = document.createElement('p');
        idea.textContent = story.idea;

        card.appendChild(title);
        card.appendChild(idea);
        storiesList.appendChild(card);
    });
}

storyForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const title = document.getElementById('title').value;
    const idea = document.getElementById('idea').value;

    try {
        const response = await fetch('/api/stories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, idea })
        });

        if (!response.ok) {
            throw new Error(`Сервер ответил с ошибкой: ${response.status}`);
        }

        storyForm.reset();
        await loadStories();
    } catch (error) {
        alert(`Не удалось создать историю: ${error.message}`);
    }
});

loadStories();
