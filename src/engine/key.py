from collections import defaultdict
from typing import Callable

import pygame


def run_hooks(key: int, hooks: dict):
    if key in hooks:
        for hook in hooks[key]:
            hook()

def register_hook(key: int, hook: Callable, all_hooks: dict):
    if key not in all_hooks:
        all_hooks[key] = [hook]
    else:
        all_hooks[key].append(hook)


class KeyStore:

    def __init__(self):
        self.keys = set()
        self.key_down_hooks = defaultdict(list)
        self.key_up_hooks = defaultdict(list)

    def register_down_hook(self, key: int, hook: Callable):
        register_hook(key, hook, self.key_down_hooks)

    def register_up_hook(self, key: int, hook: Callable):
        register_hook(key, hook, self.key_up_hooks)

    def clear_down_hooks(self, key: int):
        self.key_down_hooks.pop(key, None)

    def clear_up_hooks(self, key: int):
        self.key_up_hooks.pop(key, None)

    def get_key_down(self, key: int):
        return key in self.keys

    def process_key(self, event):
        key = event.key
        if event.type == pygame.KEYDOWN:
            self.keys.add(key)
            run_hooks(key, self.key_down_hooks)
        elif event.type == pygame.KEYUP:
            self.keys.discard(key)
            run_hooks(key, self.key_up_hooks)
