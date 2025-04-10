import sys
import time
import pygame
from pygame import Rect

from src.states.in_game import InGameState


def main():
    flags = pygame.SCALED
    pygame.init()
    pygame.display.set_mode((1920, 1080), flags)
    pygame.display.set_caption("Beyond Origins")
    pygame.display.set_icon(pygame.image.load("res/images/favicon.png"))
    clock = pygame.time.Clock()
    running = True
    fps = 60

    screen = pygame.display.get_surface()
    display_rect = Rect(0, 0, screen.get_width(), screen.get_height())
    in_game = InGameState(screen)
    current_state = in_game

    debug = True
    debug_font = pygame.font.SysFont("Consolas", 16)

    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYUP or event.type == pygame.KEYDOWN:
                current_state.ks.process_key(event)
            elif event.type >= pygame.USEREVENT:
                current_state.es.process_event(event.type)

        delta = clock.tick(fps) / 1000
        if debug:
            t_1 = time.perf_counter() * 1000
        current_state.tick(delta)
        if debug:
            t_2 = time.perf_counter() * 1000
        current_state.render(screen)
        if debug:
            t_3 = time.perf_counter() * 1000
            ft, tt, rt = t_3 - t_1, t_2 - t_1, t_3 - t_2
            ds = debug_font.render(f"{ft:.1f}ms ({ft/(1000/fps):.1f}%) d={delta:.3f} t={tt:.1f}ms r={rt:.1f}ms",
                                   True, "white")
            screen.blit(ds, (32, 32))
        pygame.display.update(display_rect)

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    main()
