function translateStatus(status: string): string {
  switch (status) {
    case 'Dostarczono':
      return 'DELIVERED';
    case 'Zaakceptowano':
      return 'ACCEPTED';
    case 'Odrzucono':
      return 'REJECTED';
    default:
      return '';
  }
}

export { translateStatus };
