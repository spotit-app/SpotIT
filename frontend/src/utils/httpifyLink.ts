function httpify(link: string): string {
  return link.startsWith('http') ? link : `https://${link}`;
}

export { httpify };
