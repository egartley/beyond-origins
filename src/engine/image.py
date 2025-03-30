from collections import defaultdict
from enum import Enum
from typing import Any

import pygame.image
from pygame import Surface


class ImageStore:

    def __init__(self):
        self.images = defaultdict(None)

    def clear(self):
        self.images.clear()

    def get(self, identifier: Any) -> Surface:
        if identifier in self.images:
            return self.images[identifier]
        return self._load(identifier)

    def _load(self, identifier: Any, alpha: bool = False) -> Surface:
        if isinstance(identifier, Enum):
            image = pygame.image.load(identifier.value)
        else:
            image = pygame.image.load(identifier)
        if alpha:
            image.convert_alpha()
        else:
            image.convert()
        self.images[identifier] = image
        return image
