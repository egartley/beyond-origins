import time
import pygame
from pygame import Surface

from src.states.in_game import InGameState


def main():
    flags = pygame.SCALED  # turn off scaled for fullscreen
    pygame.init()
    pygame.display.set_mode((1920, 1080), flags)  # 3840, 2160
    pygame.display.set_caption("Beyond Origins")
    pygame.display.set_icon(pygame.image.load("res/images/favicon.png"))
    clock = pygame.time.Clock()
    fps = 60

    screen = pygame.display.get_surface()

    debug = True
    debug_font = pygame.font.SysFont("Consolas", 16)

    in_game = InGameState(screen, debug)
    current_state = in_game

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYUP or event.type == pygame.KEYDOWN:
                current_state.ks.process_key(event)
            elif event.type >= pygame.USEREVENT:
                current_state.es.process_event(event.type)

        t1, t2, t3 = 0, 0, 0
        delta = clock.tick(fps) / 1000
        if debug:
            t1 = time.perf_counter() * 1000
        current_state.tick(delta)
        if debug:
            t2 = time.perf_counter() * 1000
        current_state.render(screen)
        if debug:
            t3 = time.perf_counter() * 1000
            ft, tt, rt = t3 - t1, t2 - t1, t3 - t2
            ds = debug_font.render(f"{ft:.1f}ms Δ{delta:.3f} t={tt:.1f}ms r={rt:.1f}ms", True, "white")
            screen.blit(ds, (32, 32))
        pygame.display.flip()

    pygame.quit()
    return

if __name__ == "__main__":
    main()
